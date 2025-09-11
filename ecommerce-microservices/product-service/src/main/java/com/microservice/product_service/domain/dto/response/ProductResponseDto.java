package com.microservice.product_service.domain.dto.response;

import com.microservice.product_service.domain.Rating;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDto {

    private UUID id;
    private String name;
    private Rating rating;
    private BigDecimal originalPrice;
    private int discount;
    private BigDecimal discountedPrice;
    private String description;
    private String shortDescription;

    @Builder.Default
    private List<String> imageUrls  = new ArrayList<>();

    private boolean inStock;
    private Integer stackQuantity;
    private String sku;
    private BigDecimal weight;

    @Builder.Default
    private List<String> tags  = new ArrayList<>();

    private boolean isActive;
    private boolean isFeatured;
    private String color;
    private String size;
    private BrandResponseDto brand;
    private CategoryResponseDto category;
}
