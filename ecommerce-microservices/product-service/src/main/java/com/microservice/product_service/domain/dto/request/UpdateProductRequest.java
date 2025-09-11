package com.microservice.product_service.domain.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProductRequest {
    private String name;

    @DecimalMin(value = "0.01", message = "Price should be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Price format is invalid")
    private BigDecimal originalPrice;

    @Max(value = 100, message = "Discount cannot exceed 100%")
    @Min(value = 0, message = "Discount cannot be negative")
    private Integer discount;

    @Size(min = 10, max = 1000, message = "Description must be between {min} and {max} characters")
    private String description;

    @Size(min = 10, max = 500, message = "Short description must be between {min} and {max} characters")
    private String shortDescription;

    @Builder.Default
    @Size(max = 10, message = "Maximum {max} images are allowed")
    private List<String> imageUrls = new ArrayList<>();

    private Boolean inStock;

    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stackQuantity;

    @Pattern(regexp = "^[A-Z0-9-]+$", message = "SKU contains only upper case letters")
    private String sku;

    @DecimalMin(value = "0.0", message = "Weight cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "weight format is invalid")
    private BigDecimal weight;

    @Size(max = 20, message = "Maximum {max} tags are allowed")
    private List<String> tags;

    private Boolean isActive;

    private Boolean isFeatured;

    @Size(max = 50, message = "Color cannot exceed {max} characters")
    private String color;

    private String size;

}