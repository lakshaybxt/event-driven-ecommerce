package com.microservice.product_service.configuration;

import com.microservice.product_service.security.HeaderBasedAuthenticationFilter;
import com.microservice.product_service.security.JwtSecurityFilter;
import com.microservice.product_service.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtSecurityFilter jwtSecurityFilter) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/product/public/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers("/api/v1/brand/public/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers("/api/v1/category/public/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers("/api/v1/product/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/brand/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/category/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/brand/**").authenticated()
                        .requestMatchers("/api/v1/category/**").authenticated()
                        .requestMatchers("/api/v1/products/**").authenticated()
                        .anyRequest().authenticated())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtSecurityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public HeaderBasedAuthenticationFilter headerBasedAuthenticationFilter() {
        return new HeaderBasedAuthenticationFilter();
    }

    @Bean
    public JwtSecurityFilter jwtSecurityFilter(JwtService jwtService) {
        return new JwtSecurityFilter(jwtService);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
