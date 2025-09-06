package com.cognivanta.user_service.service;

import com.cognivanta.user_service.domain.dto.LoginUserDto;
import com.cognivanta.user_service.domain.dto.RegisterUserDto;
import com.cognivanta.user_service.domain.dto.VerifyUserDto;
import com.cognivanta.user_service.domain.entity.User;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {
    User signup(RegisterUserDto request);
    UserDetails authenticate(LoginUserDto request);
    void verifyUser(VerifyUserDto request);
    void resenVerificationEmail(String email);
}
