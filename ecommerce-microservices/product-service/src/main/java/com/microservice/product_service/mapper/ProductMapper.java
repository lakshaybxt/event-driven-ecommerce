package com.microservice.product_service.mapper;

import com.microservice.product_service.domain.dto.response.ProductResponseDto;
import com.microservice.product_service.domain.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    ProductResponseDto toResponse(Product product);
}
