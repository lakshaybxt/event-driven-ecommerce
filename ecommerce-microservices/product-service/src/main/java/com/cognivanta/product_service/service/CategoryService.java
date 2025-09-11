package com.cognivanta.product_service.service;



import com.cognivanta.product_service.domain.dto.CreateCategoryRequestDto;
import com.cognivanta.product_service.domain.dto.UpdateCategoryRequestDto;
import com.cognivanta.product_service.domain.entity.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    Category getCategoryById(UUID categoryId);
    List<Category> findAllCategories();
    Category publishCategory(CreateCategoryRequestDto request);
    Category updateCategory(UUID categoryId, UpdateCategoryRequestDto request);
    void deleteById(UUID categoryId);
}
