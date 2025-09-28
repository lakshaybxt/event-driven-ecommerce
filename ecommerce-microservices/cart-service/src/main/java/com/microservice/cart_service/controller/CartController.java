package com.microservice.cart_service.controller;

import com.microservice.cart_service.domain.dto.AddToCartRequest;
import com.microservice.cart_service.domain.dto.CartResponseDto;
import com.microservice.cart_service.domain.entity.Cart;
import com.microservice.cart_service.mapper.CartMapper;
import com.microservice.cart_service.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartMapper cartMapper;

    @PutMapping(path = "/public/add")
    public ResponseEntity<CartResponseDto> addToCart(
            @RequestAttribute UUID userId,
            @Valid @RequestBody AddToCartRequest request
    ) {
        Cart cart = cartService.addToCart(userId, request);
        CartResponseDto response = cartMapper.toResponse(cart);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/public")
    public ResponseEntity<CartResponseDto> viewCart(@RequestAttribute UUID userId) {
        Cart cart = cartService.viewUserCart(userId);
        CartResponseDto response = cartMapper.toResponse(cart);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/remove")
    public ResponseEntity<CartResponseDto> removeFromCart(
            @RequestAttribute UUID userId,
            @RequestParam UUID productId
    ) {
        Cart cart = cartService.removeProductFromCart(userId, productId);
        CartResponseDto response = cartMapper.toResponse(cart);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/public/{productId}")
    public ResponseEntity<Void> deleteCartProduct(@PathVariable UUID productId) {
        cartService.deleteProductFromCarts(productId);
        return ResponseEntity.noContent().build();
    }

}
