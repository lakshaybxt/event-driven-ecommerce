package com.microservice.product_service.service;

import com.microservice.product_service.domain.dto.ProductSearchCriteria;
import com.microservice.product_service.domain.dto.request.CreateProductRequest;
import com.microservice.product_service.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Product createProduct(CreateProductRequest request);
    Page<Product> searchProducts(ProductSearchCriteria criteria, Pageable pageable);
}
