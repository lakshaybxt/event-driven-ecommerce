package com.cognivanta.product_service.service;

import com.cognivanta.product_service.domain.dto.CreateBrandRequestDto;
import com.cognivanta.product_service.domain.dto.UpdateBrandRequestDto;
import com.cognivanta.product_service.domain.entity.Brand;

import java.util.List;
import java.util.UUID;

public interface BrandService {
    Brand getBrandById(UUID brandId);
    List<Brand> findAllBrands();
    Brand publishBrand(CreateBrandRequestDto request);
    Brand updateBrand(UUID brandId, UpdateBrandRequestDto request);
    void deleteById(UUID brandId);
}
