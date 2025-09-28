package com.microservice.product_service.controller;

import com.microservice.product_service.domain.dto.ProductSearchCriteria;
import com.microservice.product_service.domain.dto.request.CreateProductRequest;
import com.microservice.product_service.domain.dto.request.UpdateProductRequest;
import com.microservice.product_service.domain.dto.response.ProductResponseDto;
import com.microservice.product_service.domain.entity.Product;
import com.microservice.product_service.mapper.ProductMapper;
import com.microservice.product_service.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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

    @DeleteMapping(path = "/admin/{productId}")
    public ResponseEntity<Void> softDeleteProduct(@PathVariable UUID productId) {
        prodService.softDeleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/admin/{productId}/permanent")
    public ResponseEntity<Void> permanentDeleteProduct(@PathVariable UUID productId) {
        prodService.permanentDeleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "admin/{productId}/restore")
    public ResponseEntity<ProductResponseDto> restoreProduct(@PathVariable UUID productId) {
        Product restoredProduct = prodService.restoreProduct(productId);
        ProductResponseDto response =prodMapper.toResponse(restoredProduct);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/public/search")
    public ResponseEntity<Page<ProductResponseDto>> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String brandId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(defaultValue = "false") boolean inStockOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        ProductSearchCriteria criteria = ProductSearchCriteria.builder()
                .keyword(keyword)
                .categoryId(categoryId)
                .brandId(brandId)
                .maxPrice(maxPrice)
                .minPrice(minPrice)
                .tags(tags)
                .inStockOnly(inStockOnly)
                .build();

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> products = prodService.searchProducts(criteria, pageable);
        Page<ProductResponseDto> response = products.map(prodMapper::toResponse);

        return ResponseEntity.ok(response);
    }
//
    @PutMapping(path = "/admin/{productId}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable UUID productId,
            @RequestBody UpdateProductRequest request
    ) {
        Product product = prodService.updateProduct(productId, request);
        ProductResponseDto response = prodMapper.toResponse(product);

        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/admin/{productId}/featured")
    public ResponseEntity<ProductResponseDto> toggleFeatured(@PathVariable UUID productId) {
        Product product = prodService.toggleFeatured(productId);
        ProductResponseDto response = prodMapper.toResponse(product);

        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/admin/{productId}/active")
    public ResponseEntity<ProductResponseDto> toggleActive(@PathVariable UUID productId) {
        Product product = prodService.toggleActive(productId);
        ProductResponseDto response = prodMapper.toResponse(product);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/public/{productId}/personalized")
    public ResponseEntity<List<ProductResponseDto>> getRecommendation(
            @PathVariable UUID productId,
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<Product> recommendation = prodService.getRecommendedProducts(productId, limit);
        List<ProductResponseDto> responses = recommendation.stream()
                .map(prodMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping(path = "/public/{productId}/recommendation")
    public ResponseEntity<List<ProductResponseDto>> getRecommendationByBrand(
            @PathVariable UUID productId,
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<Product> recommendation = prodService.getRecommendedByBrands(productId, limit);
        List<ProductResponseDto> responses = recommendation.stream()
                .map(prodMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping(path = "/public/{productId}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable UUID productId) {
        Product product = prodService.getProductById(productId);
        ProductResponseDto response = prodMapper.toResponse(product);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/public")
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Product> products = prodService.getAllProducts(pageable);
        Page<ProductResponseDto> response = products.map(prodMapper::toResponse);

        return ResponseEntity.ok(response);

    }

    @GetMapping(path = "/public/featured")
    public ResponseEntity<Page<ProductResponseDto>> getAllFeatured(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Product> products = prodService.getFeaturedProducts(pageable);
        Page<ProductResponseDto> response = products.map(prodMapper::toResponse);

        return ResponseEntity.ok(response);
    }


    @PostMapping(path = "/public/bulk")
    public ResponseEntity<List<ProductResponseDto>> getProductsByIds(@RequestBody List<UUID> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return ResponseEntity.badRequest().body(List.of());
        }

        List<Product> products = prodService.getAllProductsByIds(productIds);

        if (products.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<ProductResponseDto> responses = products.stream()
                .map(prodMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PostMapping(path = "/public/{productId}/reserve")
    public ResponseEntity<String> reserveStock(@PathVariable UUID productId, @RequestParam int qty) {
        boolean reserved = prodService.reserveStock(productId, qty);
        if (reserved) {
            return ResponseEntity.ok("Stock reserved successfully");
        } else {
            return ResponseEntity.badRequest().body("Insufficient stock");
        }
    }

    @PostMapping("/public/{productId}/unreserve")
    ResponseEntity<String> unreserveStock(
            @PathVariable("productId") UUID productId,
            @RequestParam("qty") int qty
    ) {
        boolean unreserved = prodService.unreserveStock(productId, qty);
        if (unreserved) {
            return ResponseEntity.ok("Stock reserved successfully");
        } else {
            return ResponseEntity.badRequest().body("Insufficient stock");
        }
    }

}
