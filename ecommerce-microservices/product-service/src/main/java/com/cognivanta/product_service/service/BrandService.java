package com.cognivanta.product_service.service;

import com.cognivanta.product_service.domain.dto.CreateUpdateBrandRequestDto;
import com.cognivanta.product_service.domain.entity.Brand;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface BrandService {
    Brand getBrandById(UUID brandId);
    List<Brand> findAllBrands();
    Brand publishBrand(CreateUpdateBrandRequestDto request);
    Brand updateBrand(UUID brandId, CreateUpdateBrandRequestDto request);
    void deleteById(UUID brandId);
}
