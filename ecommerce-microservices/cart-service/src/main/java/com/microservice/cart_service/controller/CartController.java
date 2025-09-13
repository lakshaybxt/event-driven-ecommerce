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

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartMapper cartMapper;

    @PutMapping
    public ResponseEntity<CartResponseDto> addToCart(
            @RequestAttribute UUID userId,
            @Valid @RequestBody AddToCartRequest request
    ) {
        Cart cart = cartService.addToCart(userId, request);
        CartResponseDto response = cartMapper.toResponse(cart);

        return ResponseEntity.ok(response);
    }
}
