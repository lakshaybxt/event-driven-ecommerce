package com.microservice.order_service.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.order_service.client.ProductClient;
import com.microservice.order_service.domain.OrderStatus;
import com.microservice.order_service.domain.entity.Order;
import com.microservice.order_service.kafka.event.CartEvent;
import com.microservice.order_service.kafka.event.StockEvent;
import com.microservice.order_service.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderConsumer {

    private final OrderRepository orderRepo;
    private final ProductClient prodClient;

    private final KafkaTemplate<String, CartEvent> cartKafkaTemplate;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "payment-success-events", groupId = "order-service")
    public void handleStockSuccess(String message) {
        StockEvent event = null;
        try {
            event = objectMapper.readValue(message, StockEvent.class);
        } catch (JsonProcessingException e) {
            log.error("Error processing StockEvent: {}", e.getMessage());
            throw new RuntimeException("Error processing StockEvent", e);
        }
        log.info("Stock reserved for Product {}", event.getOrderId());

        Optional<Order> orderOpt = orderRepo.findById(event.getOrderId());
        orderOpt.ifPresent(order -> {
            order.setOrderStatus(OrderStatus.PLACED);
            orderRepo.save(order);
            log.info("Order {} confirmed ", order.getId());
        });

        CartEvent cartEvent = CartEvent.builder()
                .userId(event.getUserId())
                .build();

        cartKafkaTemplate.send("cart-clear-events", cartEvent);
    }

    @Transactional
    @KafkaListener(topics = "payment-fail-events", groupId = "order-service")
    public void handleStockFailure(String message) {
        StockEvent event = null;
        try {
            event = objectMapper.readValue(message, StockEvent.class);
        } catch (JsonProcessingException e) {
            log.error("Error processing StockEvent: {}", e.getMessage());
            throw new RuntimeException("Error processing StockEvent", e);
        }
        log.info("Stock failure for Product {}", event.getOrderId());

        Order order = orderRepo.findById(event.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));


        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepo.save(order);
        log.info("Order {} cancelled ", order.getId());

        // Unreserved all products, throwing exception if any fail
        order.getOrderItems().forEach(item -> {
            ResponseEntity<String> response = prodClient.unreserveStock(item.getProductId(), item.getQuantity());
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException(
                        "Failed to unreserve stock for Product " + item.getProductId() + " in Order " + order.getId()
                );
            }
            log.info("Unreserved stock for Product {} in Order {}", item.getProductId(), order.getId());
        });
    }
}