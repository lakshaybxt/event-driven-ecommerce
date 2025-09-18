package com.microservice.order_service.kafka.listner;

import com.microservice.order_service.domain.OrderStatus;
import com.microservice.order_service.domain.entity.Order;
import com.microservice.order_service.kafka.KafkaTopics;
import com.microservice.order_service.kafka.event.StockFailureEvent;
import com.microservice.order_service.kafka.event.StockSuccessEvent;
import com.microservice.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderListener {

    private final OrderRepository orderRepo;

    @KafkaListener(topics = KafkaTopics.STOCK_SUCCESS_TOPIC, groupId = "order-service")
    public void handleStockSuccess(StockSuccessEvent event) {
        log.info("Stock reserved for Product {} Quantity {}", event.getProductId(), event.getQuantity());

        Optional<Order> orderOpt = orderRepo.findByOrderItemsProductId(event.getProductId());
        orderOpt.ifPresent(order -> {
            order.setOrderStatus(OrderStatus.PLACED);
            orderRepo.save(order);
            log.info("Order {} confirmed ", order.getId());
        });
    }

    @KafkaListener(topics = KafkaTopics.STOCK_FAILURE_TOPIC, groupId = "order-service")
    public void handleStockFailure(StockFailureEvent event) {
        log.info("Stock failure for Product {} Quantity {} Reason: {}",
        event.getProductId(), event.getQuantity(), event.getMessage());

        Optional<Order> orderOpt = orderRepo.findByOrderItemsProductId(event.getProductId());

        orderOpt.ifPresent(order -> {
            order.setOrderStatus(OrderStatus.CANCELLED);
            orderRepo.save(order);
            log.info("Order {} cancelled ", order.getId());
        });
    }
}