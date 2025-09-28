package com.microservice.product_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.util.UUID;

@FeignClient(name = "cart-service", url = "http://localhost:8082/api/v1")
public interface CartClient {

    @GetMapping("/cart/public")
    ResponseEntity<?> deleteCartProduct(UUID productId);
}
