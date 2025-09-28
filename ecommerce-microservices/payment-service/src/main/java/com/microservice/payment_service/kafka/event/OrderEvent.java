package com.microservice.payment_service.kafka.event;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEvent {

    @NotBlank(message = "User is required")
    private UUID userId;

    @NotBlank(message = "Order is required to create payment")
    private UUID orderId;

    @NotBlank(message = "Amount is required")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    private String currency;
}
