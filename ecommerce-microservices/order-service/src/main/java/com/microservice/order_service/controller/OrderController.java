package com.microservice.order_service.controller;

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

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping(path = "/checkout")
    public ResponseEntity<OrderResponseDto> checkout(
            @RequestAttribute UUID userId,
            @RequestBody @Valid CheckoutRequest request
    ) {
        Order order = orderService.checkout(userId, request);
        OrderResponseDto items = orderMapper.toResponse(order);

        return ResponseEntity.ok(items);
    }
}
