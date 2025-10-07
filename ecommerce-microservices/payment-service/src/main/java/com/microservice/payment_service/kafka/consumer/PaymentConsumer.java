package com.microservice.payment_service.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.payment_service.domain.dto.RazorpayOrderResponse;
import com.microservice.payment_service.kafka.event.OrderEvent;
import com.microservice.payment_service.kafka.event.StockEvent;
import com.microservice.payment_service.service.RazorpayService;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentConsumer {

    private final RazorpayService razorpayService;
    private final KafkaTemplate<String, StockEvent> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order-events", groupId = "payment-service")
    public void processOrder(String message) {
        OrderEvent orderEvent = null;
        try {
            orderEvent = objectMapper.readValue(message, OrderEvent.class);
            RazorpayOrderResponse response = razorpayService.createRazorPayOrder(orderEvent, orderEvent.getUserId());

            StockEvent event = StockEvent.builder()
                    .orderId(orderEvent.getOrderId())
                    .userId(orderEvent.getUserId())
                    .build();
            kafkaTemplate.send("payment-success-events", event);

        } catch (RazorpayException e) {
            StockEvent event = StockEvent.builder()
                    .orderId(orderEvent.getOrderId())
                    .userId(orderEvent.getUserId())
                    .build();
            kafkaTemplate.send("payment-fail-events", event);

        } catch (Exception e) {
            log.error("Unexpected error processing order {}: {}", orderEvent.getOrderId(), e.getMessage());
            StockEvent event = StockEvent.builder()
                    .orderId(orderEvent.getOrderId())
                    .userId(orderEvent.getUserId())
                    .build();
            kafkaTemplate.send("payment-fail-events", event);
        }
    }
}