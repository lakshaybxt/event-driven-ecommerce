package com.microservice.cart_service.service;

import java.util.List;

public interface JwtService {
    boolean validateToken(String token);
    String extractUserId(String token);
    List<String> extractRoles(String token);
}

