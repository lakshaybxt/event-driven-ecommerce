package com.microservice.product_service.service.implementation;

import com.microservice.product_service.domain.dto.request.CreateCategoryRequestDto;
import com.microservice.product_service.domain.dto.request.UpdateCategoryRequestDto;
import com.microservice.product_service.domain.entity.Category;
import com.microservice.product_service.exception.EntityAlreadyExistException;
import com.microservice.product_service.repository.CategoryRepository;
import com.microservice.product_service.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepo;

    @Override
    public Category getCategoryById(UUID categoryId) {
        return categoryRepo.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("category not found with id: " + categoryId));
    }

    @Override
    public List<Category> findAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    @Transactional
    public Category publishCategory(CreateCategoryRequestDto request) {
        if(categoryRepo.existsByNameIgnoreCase(request.getName().toLowerCase()))
            throw new EntityAlreadyExistException("Category with " + request.getName() + " already exist");

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        return categoryRepo.save(category);
    }

    @Override
    @Transactional
    public Category updateCategory(UUID categoryId, UpdateCategoryRequestDto request) {
        Category existingcategory = getCategoryById(categoryId);

        if (request.getName() != null && !request.getName().isBlank()) {
            String newName = request.getName().trim();
            if (!existingcategory.getName().equalsIgnoreCase(newName) &&
                    categoryRepo.existsByNameIgnoreCase(newName)) {
                throw new EntityAlreadyExistException("category with name '" + newName + "' already exists");
            }
            existingcategory.setName(newName);
        }

        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            existingcategory.setDescription(request.getDescription().trim());
        }

        return categoryRepo.save(existingcategory);
    }

    @Override
    public void deleteById(UUID categoryId) {
        Category existingCategory = getCategoryById(categoryId);
        categoryRepo.delete(existingCategory);
    }
}
