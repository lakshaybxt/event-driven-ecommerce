package com.microservice.product_service.controller;

import com.microservice.product_service.domain.dto.ProductSearchCriteria;
import com.microservice.product_service.domain.dto.request.CreateProductRequest;
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
}
