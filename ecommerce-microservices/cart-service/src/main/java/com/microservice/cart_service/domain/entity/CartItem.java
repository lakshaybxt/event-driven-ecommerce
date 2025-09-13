package com.microservice.cart_service.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "cartItems")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    private int quantity;
}
