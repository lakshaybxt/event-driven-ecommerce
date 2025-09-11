package com.microservice.product_service.domain.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBrandRequestDto {
    private String name;

    @Size(min = 5, max = 20, message = "Description must be between {max} and {min} characters")
    private String description;
}
