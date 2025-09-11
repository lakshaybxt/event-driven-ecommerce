package com.microservice.product_service.controller;

import com.microservice.product_service.domain.dto.request.CreateProductRequest;
import com.microservice.product_service.domain.dto.response.ProductResponseDto;
import com.microservice.product_service.domain.entity.Product;
import com.microservice.product_service.mapper.ProductMapper;
import com.microservice.product_service.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService prodService;
    private final ProductMapper prodMapper;

    @PostMapping(path = "/admin")
    public ResponseEntity<ProductResponseDto> addProduct(@Valid @RequestBody CreateProductRequest request) {
        Product product = prodService.createProduct(request);
        ProductResponseDto response = prodMapper.toResponse(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
