package com.microservice.product_service.controller;

import com.microservice.product_service.domain.dto.response.CategoryResponseDto;
import com.microservice.product_service.domain.dto.request.CreateCategoryRequestDto;
import com.microservice.product_service.domain.dto.request.UpdateCategoryRequestDto;
import com.microservice.product_service.domain.entity.Category;
import com.microservice.product_service.mapper.CategoryMapper;
import com.microservice.product_service.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @PostMapping(path = "/admin")
    public ResponseEntity<CategoryResponseDto> createCategory(@Valid @RequestBody CreateCategoryRequestDto request) {
        Category createdCategory  = categoryService.publishCategory(request);
        CategoryResponseDto response = categoryMapper.toResponse(createdCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(path = "/admin/{categoryId}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @PathVariable UUID categoryId,
            @Valid @RequestBody UpdateCategoryRequestDto request
    ) {
        Category category = categoryService.updateCategory(categoryId, request);
        CategoryResponseDto response = categoryMapper.toResponse(category);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/admin/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID categoryId) {
        categoryService.deleteById(categoryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/public/{categoryId}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable UUID categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        CategoryResponseDto response = categoryMapper.toResponse(category);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/public")
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        List<Category> categorys = categoryService.findAllCategories();
        List<CategoryResponseDto> responseList = categorys.stream()
                .map(categoryMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responseList);
    }
}
