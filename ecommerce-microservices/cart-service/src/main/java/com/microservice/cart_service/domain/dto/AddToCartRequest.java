package com.microservice.cart_service.domain.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddToCartRequest {

    @NotNull(message = "Product is required")
    private UUID productId;

    @Max(value = 100, message = "Quantity cannot exceed 100")
    @Min(value = 1, message = "Quantity cannot be zero or negative")
    private int quantity;
}
