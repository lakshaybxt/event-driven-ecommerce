package com.cognivanta.product_service.service;



import com.cognivanta.product_service.domain.dto.CreateUpdateCategoryRequestDto;
import com.cognivanta.product_service.domain.entity.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    Category getCategoryById(UUID categoryId);
    List<Category> findAllCategories();
    Category publishCategory(CreateUpdateCategoryRequestDto request);
    Category updateCategory(UUID categoryId, CreateUpdateCategoryRequestDto request);
    void deleteById(UUID categoryId);
}
