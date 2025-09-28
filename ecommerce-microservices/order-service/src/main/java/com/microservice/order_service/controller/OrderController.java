package com.microservice.order_service.controller;

import com.microservice.order_service.domain.OrderStatus;
import com.microservice.order_service.domain.dto.CheckoutRequest;
import com.microservice.order_service.domain.dto.OrderItemResponseDto;
import com.microservice.order_service.domain.dto.OrderResponseDto;
import com.microservice.order_service.domain.entity.Order;
import com.microservice.order_service.mapper.OrderMapper;
import com.microservice.order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping(path = "/public/checkout")
    public ResponseEntity<OrderResponseDto> checkout(
            @RequestAttribute UUID userId,
            @RequestBody @Valid CheckoutRequest request
    ) {
        Order order = orderService.checkout(userId, request);
        OrderResponseDto items = orderMapper.toResponse(order);

        return ResponseEntity.ok(items);
    }

    @GetMapping("/public/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(
            @RequestAttribute UUID userId,
            @PathVariable UUID orderId
    ) {
        Order order = orderService.getOrderById(userId, orderId);
        OrderResponseDto items = orderMapper.toResponse(order);

        return ResponseEntity.ok(items);
    }

    @DeleteMapping("/public/{orderId}")
    public ResponseEntity<Void> cancelOrder(
            @RequestAttribute UUID userId,
            @PathVariable UUID orderId
    ) {
        orderService.cancelOrder(userId, orderId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/admin/{orderId}/status")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
            @PathVariable UUID orderId,
            @RequestParam OrderStatus status
    ) {
        Order updatedOrder = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(orderMapper.toResponse(updatedOrder));
    }

    @GetMapping("/public")
    public ResponseEntity<List<OrderResponseDto>> getAllUserOrders(@RequestAttribute UUID userId) {
        List<Order> orders = orderService.getUserOrders(userId);
        List<OrderResponseDto> response = orders.stream()
                .map(orderMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/exists/{productId}")
    public ResponseEntity<Boolean> existsByProductId(@PathVariable UUID productId) {
        boolean exists = orderService.existsByProductId(productId);
        return ResponseEntity.ok(exists);
    }
}
