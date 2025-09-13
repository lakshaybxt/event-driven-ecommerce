package com.microservice.cart_service.mapper;

import com.microservice.cart_service.client.ProductClient;
import com.microservice.cart_service.domain.dto.CartItemResponseDto;
import com.microservice.cart_service.domain.dto.CartResponseDto;
import com.microservice.cart_service.domain.dto.ProductResponse;
import com.microservice.cart_service.domain.entity.Cart;
import com.microservice.cart_service.domain.entity.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    private final ProductClient productClient;

    public CartMapper(ProductClient productClient) {
        this.productClient = productClient;
    }

    public CartResponseDto toResponse(Cart cart) {
        if(cart == null) {
            return CartResponseDto.builder()
                    .items(List.of())
                    .totalItems(0)
                    .totalOriginalAmount(BigDecimal.ZERO)
                    .totalDiscountedAmount(BigDecimal.ZERO)
                    .totalAmount(BigDecimal.ZERO)
                    .build();
        }

        List<UUID> productIds = cart.getCartItems().stream()
                .map(CartItem::getProductId)
                .toList();

        List<ProductResponse> products = productClient.getProductsByIds(productIds);
        Map<UUID, ProductResponse> producMap = products.stream()
                .collect(Collectors.toMap(ProductResponse::getId, p -> p));

        List<CartItemResponseDto> items = cart.getCartItems().stream()
                .map(cartItem -> {
                    ProductResponse product = producMap.get(cartItem.getProductId());
                    return toCartItemResponseDto(cartItem, product);
                })
                .toList();

        int totalItems = cart.getCartItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        BigDecimal totalOriginalAmount = cart.getCartItems().stream()
                .map(item -> producMap.get(item.getProductId())
                        .getOriginalPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDiscountedAmount = cart.getCartItems().stream()
                .map(item -> producMap.get(item.getProductId())
                        .getDiscountedPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAmount = totalOriginalAmount.subtract(totalDiscountedAmount);

        return CartResponseDto.builder()
                .id(cart.getId())
                .items(items)
                .totalItems(totalItems)
                .totalOriginalAmount(totalOriginalAmount)
                .totalDiscountedAmount(totalDiscountedAmount)
                .totalAmount(totalAmount)
                .build();

    }

    private CartItemResponseDto toCartItemResponseDto(CartItem cartItem, ProductResponse product) {
        return CartItemResponseDto.builder()
                .cartItemId(cartItem.getId())
                .productId(product.getId())
                .productName(product.getName())
                .shortDescription(product.getShortDescription())
                .originalPrice(product.getOriginalPrice())
                .discountedPrice(product.getDiscountedPrice())
                .discount(product.getDiscount())
                .imageUrls(product.getImageUrls())
                .brand(product.getBrand() != null ? product.getBrand().getName() : null)
                .category(product.getCategory() != null ? product.getCategory().getName() : null)
                .quantity(cartItem.getQuantity())
                .itemTotal(product.getDiscountedPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .inStock(product.isInStock())
                .sku(product.getSku())
                .color(product.getColor())
                .size(product.getSize())
                .build();
    }
}
