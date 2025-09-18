package com.microservice.product_service.kafaka.listner;

import com.microservice.product_service.domain.entity.Product;
import com.microservice.product_service.kafaka.KafkaTopics;
import com.microservice.product_service.kafaka.event.StockFailureEvent;
import com.microservice.product_service.kafaka.event.StockSuccessEvent;
import com.microservice.product_service.kafaka.event.StockUpdateEvent;
import com.microservice.product_service.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductStockListener {

    private final ProductRepository productRepo;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = KafkaTopics.PRODUCT_STOCK_TOPIC, groupId = "product-service")
    public void handleStockUpdate(StockUpdateEvent event) {
        Product product = productRepo.findById(event.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with " + event.getProductId()));

        if(product.getStackQuantity() < event.getQuantity()) {
            StockFailureEvent failureEvent = new StockFailureEvent(
                    event.getProductId(),
                    event.getQuantity(),
                    "Insufficient stock"
            );
            kafkaTemplate.send(KafkaTopics.STOCK_FAILURE_TOPIC, failureEvent);
            return;
        }
        product.setStackQuantity(product.getStackQuantity() - event.getQuantity());
        productRepo.save(product);

        StockSuccessEvent successEvent = new StockSuccessEvent(
                event.getProductId(),
                event.getQuantity()
        );
        kafkaTemplate.send(KafkaTopics.STOCK_SUCCESS_TOPIC, successEvent);
    }
}
