package com.microservice.product_service.service;



import com.microservice.product_service.domain.dto.request.CreateCategoryRequestDto;
import com.microservice.product_service.domain.dto.request.UpdateCategoryRequestDto;
import com.microservice.product_service.domain.entity.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    Category getCategoryById(UUID categoryId);
    List<Category> findAllCategories();
    Category publishCategory(CreateCategoryRequestDto request);
    Category updateCategory(UUID categoryId, UpdateCategoryRequestDto request);
    void deleteById(UUID categoryId);
}
