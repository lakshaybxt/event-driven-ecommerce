package com.microservice.order_service.domain.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponse {
    private UUID id;
    private List<CartItemResponse> items;
    private int totalItems;
    private BigDecimal totalOriginalAmount;
    private BigDecimal totalDiscountedAmount;
    private BigDecimal totalAmount;
}
