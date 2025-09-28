package com.microservice.payment_service.service.impl;

import com.microservice.payment_service.domain.dto.PayRequestDto;
import com.microservice.payment_service.domain.dto.RazorpayOrderResponse;
import com.microservice.payment_service.domain.dto.VerifyPaymentRequest;
import com.microservice.payment_service.domain.entity.Payment;
import com.microservice.payment_service.kafka.event.OrderEvent;
import com.microservice.payment_service.repository.PaymentRepository;
import com.microservice.payment_service.service.RazorpayService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RazorpayServiceImpl implements RazorpayService {

    @Value("${razorpay.account.key}")
    private String apiKey;

    @Value("${razorpay.account.secret}")
    private String apiSecret;

    private final PaymentRepository paymentRepository;

    private RazorpayClient client;

    @PostConstruct
    public void init() throws RazorpayException {
        client = new RazorpayClient(apiKey, apiSecret);
    }

    @Override
    public RazorpayOrderResponse createRazorPayOrder(OrderEvent payRequest, UUID userId) throws RazorpayException {
        JSONObject request = new JSONObject();
        request.put("amount", payRequest.getAmount().multiply(BigDecimal.valueOf(100)));
        request.put("currency", payRequest.getCurrency());
        request.put("receipt", "order_" + System.currentTimeMillis() + userId);

        Order order = client.orders.create(request);

        if (!payRequest.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized payment request");
        }

        Payment payment = Payment.builder()
                .userId(userId)
                .orderId(payRequest.getOrderId())
                .price(payRequest.getAmount())
                .currency(payRequest.getCurrency())
                .razorpayOrderId(request.get("receipt").toString())
                .status("CREATED")
                .build();
        paymentRepository.save(payment);

        return RazorpayOrderResponse.builder()
                .orderId(order.get("id"))
                .currency(order.get("currency"))
                .amount(order.get("amount"))
                .build();
    }

    @Override
    public boolean verifyPayment(VerifyPaymentRequest request, UUID userId) {
        try {
            String payload = request.getRazorpayOrderId() + "|" + request.getRazorpayPaymentId();

            boolean isValid = verifySignature(payload, request.getRazorpaySignature());

            Payment payment = paymentRepository.findByRazorpayOrderId(request.getRazorpayOrderId())
                    .orElseThrow(() -> new RuntimeException("Payment order not found"));

            if (!payment.getUserId().equals(userId)) {
                throw new RuntimeException("Unauthorized payment verification");
            }

            if (isValid) {
                payment.setStatus("SUCCESS");
                payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
            } else {
                payment.setStatus("FAILED");
            }

            paymentRepository.save(payment);

            return isValid;

        } catch (Exception e) {
            return false;
        }
    }

    private boolean verifySignature(String payload, String signature) {
        try {
            return Utils.verifySignature(payload, signature, apiSecret);
        } catch (RazorpayException e) {
            return false;
        }
    }
}
