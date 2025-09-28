package com.microservice.order_service.service;

import com.microservice.order_service.domain.OrderStatus;
import com.microservice.order_service.domain.dto.CheckoutRequest;
import com.microservice.order_service.domain.entity.Order;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    Order checkout(UUID userId, CheckoutRequest request);
    Order getOrderById(UUID userId, UUID orderId);
    void cancelOrder(UUID userId, UUID orderId);
    Order updateOrderStatus(UUID orderId, OrderStatus status);
    List<Order> getUserOrders(UUID userId);
    boolean existsByProductId(UUID productId);
}
