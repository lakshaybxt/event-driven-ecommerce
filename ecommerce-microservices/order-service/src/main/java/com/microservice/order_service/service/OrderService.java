package com.microservice.order_service.service;

import com.microservice.order_service.domain.dto.CheckoutRequest;
import com.microservice.order_service.domain.entity.Order;
import jakarta.validation.Valid;

import java.util.UUID;

public interface OrderService {
    Order checkout(UUID userId, CheckoutRequest request);
}
