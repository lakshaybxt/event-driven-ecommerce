package com.microservice.user_service.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AuthUserResponseDto {
    private UUID id;
    private String username;
    private String email;
    private String number;
}