package com.microservice.payment_service.domain.dto;

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
public class OrderResponse {
    private UUID orderId;
//    private OrderStatus orderStatus;
//    private PaymentStatus paymentStatus;
//    private PaymentMethod paymentMethod;
    private BigDecimal finalPrice;
    private List<OrderItemResponse> orderItems;
//    private AddressDto shippingAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
