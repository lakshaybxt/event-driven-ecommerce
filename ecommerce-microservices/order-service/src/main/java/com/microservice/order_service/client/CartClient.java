package com.microservice.order_service.client;

import com.microservice.order_service.domain.dto.CartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "cart-service", url = "http://localhost:8082/api/v1")
public interface CartClient {

    @GetMapping("/cart/public")
    CartResponse viewCart(@RequestAttribute UUID userId);
}
