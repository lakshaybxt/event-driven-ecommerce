package com.microservice.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "product-service", url = "http://localhost:8081/api/v1")
public interface ProductClient {
    @PostMapping("/public/{productId}/reserve")
    ResponseEntity<String> reserveStock(@PathVariable("productId") UUID productId, @RequestParam("qty") int qty);

    @PostMapping("/public/{productId}/unreserve")
    ResponseEntity<String> unreserveStock(@PathVariable("productId") UUID productId, @RequestParam("qty") int qty);
}
