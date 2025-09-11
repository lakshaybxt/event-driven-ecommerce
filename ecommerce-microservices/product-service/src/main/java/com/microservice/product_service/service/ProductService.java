package com.microservice.product_service.service;

import com.microservice.product_service.domain.dto.request.CreateProductRequest;
import com.microservice.product_service.domain.entity.Product;

public interface ProductService {
    Product createProduct(CreateProductRequest request);
}
