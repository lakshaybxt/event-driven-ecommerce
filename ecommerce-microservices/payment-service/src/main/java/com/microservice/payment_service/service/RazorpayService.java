package com.microservice.payment_service.service;

import com.microservice.payment_service.domain.dto.PayRequestDto;
import com.microservice.payment_service.domain.dto.RazorpayOrderResponse;
import com.microservice.payment_service.domain.dto.VerifyPaymentRequest;
import com.microservice.payment_service.kafka.event.OrderEvent;
import com.razorpay.RazorpayException;

import java.util.UUID;

public interface RazorpayService {
    RazorpayOrderResponse createRazorPayOrder(OrderEvent payRequest, UUID userId) throws RazorpayException;
    boolean verifyPayment(VerifyPaymentRequest request, UUID userId);
}
