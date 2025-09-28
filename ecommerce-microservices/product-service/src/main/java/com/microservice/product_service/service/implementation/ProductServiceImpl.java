package com.microservice.product_service.service.implementation;

import com.microservice.product_service.client.CartClient;
import com.microservice.product_service.client.OrderClient;
import com.microservice.product_service.domain.dto.ProductSearchCriteria;
import com.microservice.product_service.domain.dto.request.CreateProductRequest;
import com.microservice.product_service.domain.dto.request.UpdateProductRequest;
import com.microservice.product_service.domain.entity.Brand;
import com.microservice.product_service.domain.entity.Category;
import com.microservice.product_service.domain.entity.Product;
import com.microservice.product_service.exception.BusinessRuleException;
import com.microservice.product_service.exception.EntityAlreadyExistException;
import com.microservice.product_service.repository.ProductRepository;
import com.microservice.product_service.service.BrandService;
import com.microservice.product_service.service.CategoryService;
import com.microservice.product_service.service.ProductService;
import com.microservice.product_service.specification.ProductSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final BrandService brandService;
    private final CategoryService categoryService;

    private final OrderClient orderClient;
    private final CartClient cartClient;

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

    @Override
    public Page<Product> searchProducts(ProductSearchCriteria criteria, Pageable pageable) {
        return productRepo.findAll(ProductSpecification.withCriteria(criteria), pageable);
    }
//
    @Override
    public List<Product> getRecommendedProducts(UUID productId, int limit) {
        Product product = getProductById(productId);
        Pageable pageable = PageRequest.of(0, limit);

        return productRepo.findByCategoryAndIdNotAndTagsIn(product.getCategory(), productId, product.getTags(), pageable);
    }

    @Override
    public List<Product> getRecommendedByBrands(UUID productId, int limit) {
        Product product = getProductById(productId);
        Pageable pageable = PageRequest.of(0, limit);

        return productRepo.findByBrandAndIdNot(product.getBrand(), productId, pageable);
    }

    @Override
    public Product getProductById(UUID productId) {
        return productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with " + productId));
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepo.findAll(pageable);
    }

    @Override
    public Page<Product> getFeaturedProducts(Pageable pageable) {
        return productRepo.findAllByFeatured(true, pageable);
    }

    @Override
    public Product updateProduct(UUID productId, UpdateProductRequest request) {
        Product existingProduct = getProductById(productId);

        if(request.getName() != null) {
            existingProduct.setName(request.getName());
        }

        if(request.getInStock() != null) {
            existingProduct.setInStock(request.getInStock());
        }

        if(request.getIsActive() != null) {
            existingProduct.setActive(request.getIsActive());
        }

        if(request.getIsFeatured() != null) {
            existingProduct.setFeatured(request.getIsFeatured());
        }

        if(request.getDescription() != null) {
            existingProduct.setDescription(request.getDescription());
        }

        if(request.getShortDescription() != null) {
            existingProduct.setShortDescription(request.getShortDescription());
        }

        if(request.getColor() != null) {
            existingProduct.setColor(request.getColor());
        }

        if(request.getSize() != null) {
            existingProduct.setSize(request.getSize());
        }

        if(request.getSku() != null) {
            existingProduct.setSku(request.getSku());
        }

        if(request.getDiscount() != null) {
            existingProduct.setDiscount(request.getDiscount());
        }

        if(request.getOriginalPrice() != null) {
            existingProduct.setOriginalPrice(request.getOriginalPrice());
        }

        if(request.getTags() != null) {
            existingProduct.setTags(request.getTags());
        }

        if(request.getWeight() != null) {
            existingProduct.setWeight(request.getWeight());
        }

        if(request.getImageUrls() != null) {
            existingProduct.setImageUrls(request.getImageUrls());
        }

        if(request.getStackQuantity() != null) {
            existingProduct.setStackQuantity(request.getStackQuantity());
        }

        return productRepo.save(existingProduct);

    }

    @Override
    public Product toggleFeatured(UUID productId) {
        Product existingProduct = getProductById(productId);
        existingProduct.setFeatured(!existingProduct.isFeatured());
        return productRepo.save(existingProduct);
    }

    @Override
    public Product toggleActive(UUID productId) {
        Product existingProduct = getProductById(productId);
        existingProduct.setActive(!existingProduct.isActive());
        return productRepo.save(existingProduct);
    }

    @Override
    public void softDeleteProduct(UUID productId) {

        Product existingProduct = getProductById(productId);

        existingProduct.setActive(false);
        existingProduct.setFeatured(false);
        existingProduct.setInStock(false);

        productRepo.save(existingProduct);
    }

    @Transactional
    @Override
    public void permanentDeleteProduct(UUID productId) {
        Product existingProduct = getProductById(productId);

        boolean hasAnyOrder = orderClient.existsByProductId(productId);

        if(hasAnyOrder) {
            throw new BusinessRuleException("Cannot permanently delete product with order history");
        }

        if(existingProduct.isActive() || existingProduct.getUpdatedAt().isAfter(LocalDateTime.now().minusDays(15))) {
            throw new BusinessRuleException("Product must be inactive for at least 15 days before permanent deletion");
        }

        handleProductDeletion(existingProduct);

        productRepo.delete(existingProduct);

    }

    @Override
    public Product restoreProduct(UUID productId) {
        Product product = getProductById(productId);

        product.setActive(true);
        product.setInStock(true);

        return productRepo.save(product);
    }

    @Override
    public List<Product> getAllProductsByIds(List<UUID> productIds) {
        return productRepo.findAllById(productIds);
    }

    @Transactional
    @Override
    public boolean reserveStock(UUID productId, int qty) {
        int updated = productRepo.reserveStock(productId, qty);
        return updated > 0;
    }

    @Transactional
    @Override
    public boolean unreserveStock(UUID productId, int qty) {
        int updated = productRepo.unreserveStock(productId, qty);
        return updated > 0;
    }

    private void handleProductDeletion(Product product) {
        cartClient.deleteCartProduct(product.getId());
//        wishlistItemRepository.deleteProductById(product.getId());
    }
}
