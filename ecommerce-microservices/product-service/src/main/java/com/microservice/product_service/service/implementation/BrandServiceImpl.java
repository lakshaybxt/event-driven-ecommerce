package com.microservice.product_service.service.implementation;

import com.microservice.product_service.domain.dto.request.CreateBrandRequestDto;
import com.microservice.product_service.domain.dto.request.UpdateBrandRequestDto;
import com.microservice.product_service.domain.entity.Brand;
import com.microservice.product_service.exception.EntityAlreadyExistException;
import com.microservice.product_service.repository.BrandRepository;
import com.microservice.product_service.service.BrandService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepo;

    @Override
    public Brand getBrandById(UUID brandId) {
        return brandRepo.findById(brandId)
                .orElseThrow(() -> new EntityNotFoundException("Brand not found with id: " + brandId));
    }

    @Override
    public List<Brand> findAllBrands() {
        return brandRepo.findAll();
    }

    @Override
    @Transactional
    public Brand publishBrand(CreateBrandRequestDto request) {
        if(brandRepo.existsByNameIgnoreCase(request.getName().toLowerCase()))
            throw new EntityAlreadyExistException("Brand with " + request.getName() + " alreay exist");

        Brand brand = Brand.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        return brandRepo.save(brand);
    }

    @Override
    @Transactional
    public Brand updateBrand(UUID brandId, UpdateBrandRequestDto request) {
        Brand existingBrand = getBrandById(brandId);

        if (request.getName() != null && !request.getName().isBlank()) {
            String newName = request.getName().trim();
            if (!existingBrand.getName().equalsIgnoreCase(newName) &&
                    brandRepo.existsByNameIgnoreCase(newName)) {
                throw new EntityAlreadyExistException("Brand with name '" + newName + "' already exists");
            }
            existingBrand.setName(newName);
        }

        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            existingBrand.setDescription(request.getDescription().trim());
        }

        return brandRepo.save(existingBrand);
    }

    @Override
    public void deleteById(UUID brandId) {
        //TODO: Before deleting it I have to check if someone ordered from the brand or not
        Brand existingBrand = getBrandById(brandId);
        brandRepo.delete(existingBrand);
    }
}
