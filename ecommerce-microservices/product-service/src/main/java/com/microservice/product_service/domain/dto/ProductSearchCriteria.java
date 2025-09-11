package com.microservice.product_service.domain.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSearchCriteria {
    String keyword;
    String categoryId;
    String brandId;
    BigDecimal minPrice;
    BigDecimal maxPrice;
    List<String> tags;
    boolean inStockOnly;
}