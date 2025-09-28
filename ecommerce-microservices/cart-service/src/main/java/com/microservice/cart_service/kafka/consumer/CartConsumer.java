package com.microservice.cart_service.kafka.consumer;

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

    @KafkaListener(topics = "cart-clear-events", groupId = "cart-service", containerFactory = "kafkaListenerContainerFactory")
    public void clearCart(CartEvent cartEvent) {
        log.info("Clearing cart for user {}", cartEvent.getUserId());
        cartRepository.deleteByUserId(cartEvent.getUserId());
    }
}
