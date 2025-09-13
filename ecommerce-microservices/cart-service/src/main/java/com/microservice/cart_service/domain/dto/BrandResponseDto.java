package com.microservice.cart_service.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BrandResponseDto {
    private UUID id;
    private String name;
    private String description;
}
