package com.microservice.cart_service.repository;

import com.microservice.cart_service.domain.entity.Cart;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUserId(UUID userId);
    boolean existsByUserId(UUID userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.productId = :productId")
    void deleteByProductId(@Param("productId") UUID productId);

    void deleteByUserId(UUID userId);
}
