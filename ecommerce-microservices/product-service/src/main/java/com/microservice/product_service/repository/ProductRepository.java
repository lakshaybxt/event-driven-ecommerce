package com.microservice.product_service.repository;

import com.microservice.product_service.domain.entity.Brand;
import com.microservice.product_service.domain.entity.Category;
import com.microservice.product_service.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameAndBrandAndCategory(String name, Brand brand, Category category);
    Page<Product> findAll(Specification<Product> productSpecification, Pageable pageable);
    List<Product> findByCategoryAndIdNotAndTagsIn(Category category, UUID productId, List<String> tags, Pageable pageable);
    List<Product> findByBrandAndIdNot(Brand brand, UUID id, Pageable pageable);
    Page<Product> findAllByFeatured(boolean b, Pageable pageable);
    /*
      If you want to run INSERT, UPDATE, or DELETE,
      you must mark the query with @Modifying
    */
    @Modifying
    @Query("UPDATE Product p SET p.stackQuantity = p.stackQuantity - :qty " +
            "WHERE p.id = :productId AND p.stackQuantity >= :qty")
    int reserveStock(@Param("productId") UUID productId, @Param("qty") int qty);

    @Modifying
    @Query("UPDATE Product p SET p.stackQuantity = p.stackQuantity + :qty " +
            "WHERE p.id = :productId")
    int unreserveStock(@Param("productId") UUID productId, @Param("qty") int qty);
}
