package com.microservice.product_service.service;

import com.microservice.product_service.domain.dto.request.CreateBrandRequestDto;
import com.microservice.product_service.domain.dto.request.UpdateBrandRequestDto;
import com.microservice.product_service.domain.entity.Brand;

import java.util.List;
import java.util.UUID;

public interface BrandService {
    Brand getBrandById(UUID brandId);
    List<Brand> findAllBrands();
    Brand publishBrand(CreateBrandRequestDto request);
    Brand updateBrand(UUID brandId, UpdateBrandRequestDto request);
    void deleteById(UUID brandId);
}
