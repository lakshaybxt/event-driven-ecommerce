package com.microservice.product_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.util.UUID;

@FeignClient(name = "cart-service", url = "http://localhost:8083/api/v1")
public interface OrderClient {

    @GetMapping("/cart/public")
    boolean existsByProductId( UUID productId);
}
