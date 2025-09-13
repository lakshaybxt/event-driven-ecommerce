package com.microservice.cart_service.service.implementation;

import com.microservice.cart_service.domain.dto.AddToCartRequest;
import com.microservice.cart_service.domain.entity.Cart;
import com.microservice.cart_service.domain.entity.CartItem;
import com.microservice.cart_service.repository.CartRepository;
import com.microservice.cart_service.service.CartService;
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
}
