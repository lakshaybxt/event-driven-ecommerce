package com.microservice.cart_service.service;

import com.microservice.cart_service.domain.dto.AddToCartRequest;
import com.microservice.cart_service.domain.entity.Cart;
import jakarta.validation.Valid;

import java.util.UUID;

public interface CartService {
    Cart addToCart(UUID userId, AddToCartRequest request);
    Cart viewUserCart(UUID userId);
    Cart removeProductFromCart(UUID userId, UUID productId);
    void deleteProductFromCarts(UUID productId);
}
