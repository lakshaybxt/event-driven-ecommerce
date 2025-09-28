package com.microservice.payment_service.controller;

import com.microservice.payment_service.domain.dto.PayRequestDto;
import com.microservice.payment_service.domain.dto.RazorpayOrderResponse;
import com.microservice.payment_service.domain.dto.VerifyPaymentRequest;
import com.microservice.payment_service.kafka.event.OrderEvent;
import com.microservice.payment_service.service.RazorpayService;
import com.razorpay.RazorpayException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/payment/public")
public class PaymentController {

    private final RazorpayService razorpayService;

    @PostMapping(path = "/create-order")
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderEvent payRequest,
            @RequestAttribute UUID userId
    ) {
        try {
            RazorpayOrderResponse response = razorpayService.createRazorPayOrder(payRequest, userId);
            return ResponseEntity.ok(response);
        } catch (RazorpayException e) {
            return ResponseEntity.status(502).body("Payment gateway error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Something went wrong: " + e.getMessage());
        }
    }

    @PostMapping(path = "/verify-payment")
    public ResponseEntity<?> verifyPayment(
            @Valid @RequestBody VerifyPaymentRequest request,
            @RequestAttribute UUID userId
    ) {
        boolean isValid = razorpayService.verifyPayment(request, userId);

        if (isValid) {
            return ResponseEntity.ok("Payment verified successfully");
        } else {
            return ResponseEntity.status(400).body("Payment verification failed");
        }
    }
}
