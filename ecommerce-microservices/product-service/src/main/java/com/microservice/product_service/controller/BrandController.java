package com.microservice.product_service.controller;

import com.microservice.product_service.domain.dto.response.BrandResponseDto;
import com.microservice.product_service.domain.dto.request.CreateBrandRequestDto;
import com.microservice.product_service.domain.dto.request.UpdateBrandRequestDto;
import com.microservice.product_service.domain.entity.Brand;
import com.microservice.product_service.mapper.BrandMapper;
import com.microservice.product_service.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;
    private final BrandMapper brandMapper;

    @PostMapping(path = "/admin")
    public ResponseEntity<BrandResponseDto> createBrand(@Valid @RequestBody CreateBrandRequestDto request) {
        Brand createdBrand  = brandService.publishBrand(request);
        BrandResponseDto response = brandMapper.toResponse(createdBrand);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(path = "/admin/{brandId}")
    public ResponseEntity<BrandResponseDto> updateBrand(
            @PathVariable UUID brandId,
            @Valid @RequestBody UpdateBrandRequestDto request
    ) {
        Brand brand = brandService.updateBrand(brandId, request);
        BrandResponseDto response = brandMapper.toResponse(brand);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/admin/{brandId}")
    public ResponseEntity<Void> deleteBrand(@PathVariable UUID brandId) {
        brandService.deleteById(brandId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/public/{brandId}")
    public ResponseEntity<BrandResponseDto> getBrandById(@PathVariable UUID brandId) {
        Brand brand = brandService.getBrandById(brandId);
        BrandResponseDto response = brandMapper.toResponse(brand);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/public")
    public ResponseEntity<List<BrandResponseDto>> getAllBrands() {
        List<Brand> brands = brandService.findAllBrands();
        List<BrandResponseDto> responseList = brands.stream()
                .map(brandMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responseList);
    }
}
