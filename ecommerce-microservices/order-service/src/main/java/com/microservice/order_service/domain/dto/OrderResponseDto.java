package com.microservice.order_service.domain.dto;

import com.microservice.order_service.domain.OrderStatus;
import com.microservice.order_service.domain.PaymentMethod;
import com.microservice.order_service.domain.PaymentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class OrderResponseDto {
    private UUID orderId;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private BigDecimal finalPrice;
    private List<OrderItemResponseDto> orderItems;
    private AddressDto shippingAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
