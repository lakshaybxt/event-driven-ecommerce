package com.microservice.order_service.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddressDto {

    @NotBlank(message = "Steet number is required")
    private String streetNo;

    private String houseNo;

    @NotBlank(message = "Area is required")
    private String area;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Postal code is required")
    private String postalCode;

    @NotBlank(message = "Postal code is required")
    private String country;
}
