package com.microservice.cart_service.service.implementation;

import com.microservice.cart_service.domain.dto.AddToCartRequest;
import com.microservice.cart_service.domain.entity.Cart;
import com.microservice.cart_service.domain.entity.CartItem;
import com.microservice.cart_service.repository.CartRepository;
import com.microservice.cart_service.service.CartService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepo;

    @Override
    @Transactional
    public Cart addToCart(UUID userId, AddToCartRequest request) {
        Optional<Cart> optionalCart = cartRepo.findByUserId(userId);
        Cart cart;

        if(optionalCart.isEmpty()) {
            cart = Cart.builder()
                    .userId(userId)
                    .cartItems(new ArrayList<>())
                    .build();

            cart = cartRepo.save(cart);
        } else {
            cart = optionalCart.get();
        }

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(request.getProductId()))
                .findFirst();

        if(existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + request.getQuantity());
        } else {
            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .productId(request.getProductId())
                    .quantity(request.getQuantity())
                    .build();
            cart.getCartItems().add(cartItem);
        }

        return cartRepo.save(cart);
    }

    @Override
    public Cart viewUserCart(UUID userId) {
        return cartRepo.findByUserId(userId)
                .orElseGet( () -> {
                        Cart newCart = Cart.builder()
                                .userId(userId)
                                .cartItems(new ArrayList<>())
                                .build();
                            return cartRepo.save(newCart);
                });
    }

    @Override
    public Cart removeProductFromCart(UUID userId, UUID productId) {
        Cart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found with the id: " + userId));

        boolean removed = cart.getCartItems().removeIf(item ->
                item.getProductId().equals(productId));

        if(!removed) {
            throw new EntityNotFoundException("Product not found in cart");
        }

        return cartRepo.save(cart);
    }

    @Override
    public void deleteProductFromCarts(UUID productId) {
        cartRepo.deleteByProductId(productId);
    }
}
