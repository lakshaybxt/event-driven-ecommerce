package com.microservice.payment_service.domain.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class PayRequestDto {

    @NotBlank(message = "Amount is required")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotBlank(message = "Order is required to create payment")
    private UUID orderId;

    @NotBlank(message = "User is required")
    private UUID userId;
}
