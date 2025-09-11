package com.microservice.user_service.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {

    @NotBlank(message = "Username is required")
    private String username;

    @Email(message = "Email format is not valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "User mobile number is required")
    @Pattern(
            regexp = "^(\\+91|91)?[5-9]\\d{9}$",
            message = "Invalid Indian mobile number"
    )
    private String number;

    @Size(min = 8, max = 20, message = "Password must be between {max} and {min} characters")
    private String password;
}
