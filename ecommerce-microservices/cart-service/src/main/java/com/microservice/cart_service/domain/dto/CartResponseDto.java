package com.microservice.cart_service.domain.dto;


import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponseDto {
    private UUID id;
    private List<CartItemResponseDto> items;
    private int totalItems;
    private BigDecimal totalOriginalAmount;
    private BigDecimal totalDiscountedAmount;
    private BigDecimal totalAmount;
}
