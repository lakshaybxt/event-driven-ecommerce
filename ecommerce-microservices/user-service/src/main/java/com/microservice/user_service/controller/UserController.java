package com.microservice.user_service.controller;

import com.microservice.user_service.domain.dto.LoginResponse;
import com.microservice.user_service.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final AuthenticationService authService;

    @PutMapping("/update-role")
    public ResponseEntity<LoginResponse> changeRole(
            @RequestAttribute UUID userId
    ) {
        LoginResponse response = authService.changeRoleToAdmin(userId);
        return ResponseEntity.ok(response);
    }
}
