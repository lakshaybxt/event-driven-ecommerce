package com.microservice.user_service.controller;

import com.microservice.user_service.domain.dto.*;
import com.microservice.user_service.domain.entity.User;
import com.microservice.user_service.mapper.UserMapper;
import com.microservice.user_service.service.AuthenticationService;
import com.microservice.user_service.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @PostMapping(path = "/signup")
    public ResponseEntity<AuthUserResponseDto> register(@Valid @RequestBody RegisterUserDto request) {
        User registeredUser = authService.signup(request);
        AuthUserResponseDto response = userMapper.toResponseDto(registeredUser);

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginUserDto request) {
        UserDetails authenticatedUser = authService.authenticate(request);
        LoginResponse loginResponse = LoginResponse.builder()
                .token(jwtService.generateToken(authenticatedUser))
                .expiration(jwtService.getExpirationTime())
                .build();
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping(path = "/verify")
    public ResponseEntity<?> verifyUser(@Valid @RequestBody VerifyUserDto request) {
        try {
            authService.verifyUser(request);
            return ResponseEntity.ok("Account verified successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(path = "/resend")
    public ResponseEntity<?> verifyUser(@RequestParam String email) {
        try {
            authService.resenVerificationEmail(email);
            return ResponseEntity.ok("Verification code sent");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
