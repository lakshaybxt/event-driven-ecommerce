package com.microservice.order_service.domain.dto;

import com.microservice.order_service.domain.AddressSnap;
import com.microservice.order_service.domain.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckoutRequest {
    @NotNull(message = "Address Required")
    private AddressSnap addressSnap;

    @NotNull(message = "Payment Method need to be selected")
    private PaymentMethod paymentMethod;
}
