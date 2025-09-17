package com.microservice.order_service.client;

import com.microservice.order_service.domain.dto.CartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "cart-service")
public interface CartClient {

    @GetMapping("/api/v1/cart/public")
    CartResponse viewCart(@RequestAttribute UUID userId);
}
