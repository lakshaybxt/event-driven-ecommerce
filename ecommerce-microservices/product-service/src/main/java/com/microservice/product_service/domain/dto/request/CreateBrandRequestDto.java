package com.microservice.product_service.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateBrandRequestDto {

    @NotBlank(message = "Brand name is required")
    private String name;

    @NotBlank(message = "Brand description required")
    @Size(min = 5, max = 20, message = "Description must be between {max} and {min} characters")
    private String description;
}
