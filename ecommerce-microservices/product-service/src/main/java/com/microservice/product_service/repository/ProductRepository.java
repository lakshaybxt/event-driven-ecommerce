package com.microservice.product_service.repository;

import com.microservice.product_service.domain.entity.Brand;
import com.microservice.product_service.domain.entity.Category;
import com.microservice.product_service.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameAndBrandAndCategory(String name, Brand brand, Category category);
    Page<Product> findAll(Specification<Product> productSpecification, Pageable pageable);

}
