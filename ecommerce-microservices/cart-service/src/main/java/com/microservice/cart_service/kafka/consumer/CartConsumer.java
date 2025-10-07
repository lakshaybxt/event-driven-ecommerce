package com.microservice.cart_service.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.cart_service.kafka.event.CartEvent;
import com.microservice.cart_service.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartConsumer {

    private final CartRepository cartRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "cart-clear-events", groupId = "cart-service")
    public void clearCart(String message) {
        try {
            CartEvent cartEvent = objectMapper.readValue(message, CartEvent.class);
            log.info("Clearing cart for user {}", cartEvent.getUserId());
            cartRepository.deleteByUserId(cartEvent.getUserId());
        } catch (JsonProcessingException e) {
            log.error("Error processing CartEvent: {}", e.getMessage());
            throw new RuntimeException("Error processing CartEvent", e);
        }
    }
}
