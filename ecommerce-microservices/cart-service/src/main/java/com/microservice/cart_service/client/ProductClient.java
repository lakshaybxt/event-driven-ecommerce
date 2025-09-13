package com.microservice.cart_service.client;

import com.microservice.cart_service.domain.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/v1/product/public/{productId}")
    ProductResponse getProductById(@PathVariable UUID productId);

    @PostMapping("/api/v1/product/public/bulk")
    List<ProductResponse> getProductsByIds(@RequestBody List<UUID> productIds);

}
