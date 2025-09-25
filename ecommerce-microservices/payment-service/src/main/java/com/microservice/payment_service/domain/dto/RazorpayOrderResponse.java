package com.microservice.payment_service.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RazorpayOrderResponse {
    private String orderId;
    private String currency;
    private int amount;
}
