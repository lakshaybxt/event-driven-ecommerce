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
public class CartItemResponseDto {
    private UUID cartItemId;
    private UUID productId;
    private String productName;
    private String shortDescription;
    private BigDecimal originalPrice;
    private BigDecimal discountedPrice;
    private int discount;
    private List<String> imageUrls;
    private String brand;
    private String category;
    private int quantity;
    private BigDecimal itemTotal;
    private boolean inStock;
    private String sku;
    private String color;
    private String size;
}
