package com.microservice.product_service.mapper;

import com.microservice.product_service.domain.dto.response.CategoryResponseDto;
import com.microservice.product_service.domain.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    CategoryResponseDto toResponse(Category category);
}
