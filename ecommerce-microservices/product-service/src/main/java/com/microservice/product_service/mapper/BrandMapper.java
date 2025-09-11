package com.microservice.product_service.mapper;

import com.microservice.product_service.domain.dto.response.BrandResponseDto;
import com.microservice.product_service.domain.entity.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BrandMapper {
    BrandResponseDto toResponse(Brand brand);
}
