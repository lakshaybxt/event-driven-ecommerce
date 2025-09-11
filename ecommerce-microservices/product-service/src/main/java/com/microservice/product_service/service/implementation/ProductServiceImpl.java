package com.microservice.product_service.service.implementation;

import com.microservice.product_service.domain.dto.request.CreateProductRequest;
import com.microservice.product_service.domain.entity.Brand;
import com.microservice.product_service.domain.entity.Category;
import com.microservice.product_service.domain.entity.Product;
import com.microservice.product_service.exception.EntityAlreadyExistException;
import com.microservice.product_service.repository.ProductRepository;
import com.microservice.product_service.service.BrandService;
import com.microservice.product_service.service.CategoryService;
import com.microservice.product_service.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final BrandService brandService;
    private final CategoryService categoryService;

    @Override
    @Transactional
    public Product createProduct(CreateProductRequest request) {
        Brand brand = brandService.getBrandById(request.getBrandId());
        Category category = categoryService.getCategoryById(request.getCategoryId());

        if(productRepo.existsByNameAndBrandAndCategory(request.getName(), brand, category)) {
            throw new EntityAlreadyExistException(
                    String.format("Product '%s' already exists for brand '%s' in category '%s'",
                            request.getName(), brand.getName(), category.getName())
            );
        }

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .shortDescription(request.getShortDescription())
                .originalPrice(request.getOriginalPrice())
                .discount(request.getDiscount())
                .imageUrls(request.getImageUrls())
                .color(request.getColor())
                .size(request.getSize())
                .sku(request.getSku())
                .tags(request.getTags())
                .weight(request.getWeight())
                .inStock(request.isInStock())
                .isFeatured(request.isFeatured())
                .isActive(request.isActive())
                .stackQuantity(request.getStackQuantity())
                .brand(brand)
                .category(category)
                .build();

        return productRepo.save(product);
    }
}
