package com.microservice.product_service.domain.entity;

import com.microservice.product_service.domain.Rating;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Rating rating;

    @Column(nullable = false, precision = 10, scale = 2)
    @Min(value = 1, message = "Original price can neither be zero nor negative")
    private BigDecimal originalPrice;

    @Min(value = 0, message = "Discount cannot be negative")
    @Max(value = 100, message = "Discount cannot be exceed 100%")
    private int discount;

    @Column(precision = 10, scale = 2)
    private BigDecimal discountedPrice;

    // Full description - for product detail pages
    @Column(nullable = false, length = 1000)
    private String description;

    // Short description - for product cards/lists
    @Column(nullable = false, length = 500)
    private String shortDescription;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url", length = 2048)
    @Builder.Default
    private List<String> imageUrls = new ArrayList<>();

    @Builder.Default
    private boolean inStock = true;

    @Builder.Default
    private Integer stackQuantity = 0;

    @Column(unique = true, length = 100)
    private String sku;

    @Column(precision = 8, scale = 2)
    private BigDecimal weight;

    @ElementCollection
    @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tag", length = 50)
    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @Column(nullable = false)
    @Builder.Default
    private boolean isActive = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean isFeatured = false;

    @Column(length = 50)
    private String color;

    @Column(length = 50)
    private String size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

//    @Builder.Default
//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<CartItem> cartItems = new ArrayList<>();
//
//    @ManyToMany(mappedBy = "products")
//    private Set<Wishlist> wishlists = new HashSet<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        calculateDiscountedPrice();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        calculateDiscountedPrice();
    }

    private void calculateDiscountedPrice() {
        if(originalPrice != null && discount > 0) {
            BigDecimal discountAmount = originalPrice.multiply(BigDecimal.valueOf(discount))
                    .divide(BigDecimal.valueOf(100));
            this.discountedPrice = originalPrice.subtract(discountAmount);
        } else {
            this.discountedPrice = originalPrice;
        }
    }
}
