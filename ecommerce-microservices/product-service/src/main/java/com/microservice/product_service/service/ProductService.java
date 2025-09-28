package com.microservice.product_service.service;

import com.microservice.product_service.domain.dto.ProductSearchCriteria;
import com.microservice.product_service.domain.dto.request.CreateProductRequest;
import com.microservice.product_service.domain.dto.request.UpdateProductRequest;
import com.microservice.product_service.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    Product createProduct(CreateProductRequest request);
    Page<Product> searchProducts(ProductSearchCriteria criteria, Pageable pageable);
    List<Product> getRecommendedProducts(UUID productId, int limit);
    List<Product> getRecommendedByBrands(UUID productId, int limit);
    Product getProductById(UUID productId);
    Page<Product> getAllProducts(Pageable pageable);
    Page<Product> getFeaturedProducts(Pageable pageable);
    Product updateProduct(UUID productId, UpdateProductRequest request);
    Product toggleFeatured(UUID productId);
    Product toggleActive(UUID productId);
    void softDeleteProduct(UUID productId);
    void permanentDeleteProduct(UUID productId);
    Product restoreProduct(UUID productId);
    List<Product> getAllProductsByIds(List<UUID> productIds);
    boolean reserveStock(UUID productId, int qty);
    boolean unreserveStock(UUID productId, int qty);
}
