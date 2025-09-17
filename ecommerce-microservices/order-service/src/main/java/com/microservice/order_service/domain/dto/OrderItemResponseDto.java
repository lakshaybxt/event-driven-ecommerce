package com.microservice.order_service.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
public class OrderItemResponseDto {
    private String productName;
    private UUID productId;
    private int quantity;
    private BigDecimal priceAtPurchase;
}
