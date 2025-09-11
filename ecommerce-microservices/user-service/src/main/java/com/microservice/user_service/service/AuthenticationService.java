package com.microservice.user_service.service;

import com.microservice.user_service.domain.dto.LoginResponse;
import com.microservice.user_service.domain.dto.LoginUserDto;
import com.microservice.user_service.domain.dto.RegisterUserDto;
import com.microservice.user_service.domain.dto.VerifyUserDto;
import com.microservice.user_service.domain.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface AuthenticationService {
    User signup(RegisterUserDto request);
    UserDetails authenticate(LoginUserDto request);
    void verifyUser(VerifyUserDto request);
    void resenVerificationEmail(String email);
    LoginResponse changeRoleToAdmin(UUID userId);
    User getUserById(UUID userId);
}
