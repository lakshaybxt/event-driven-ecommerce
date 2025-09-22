package com.microservice.order_service.service.implementation;

import com.microservice.order_service.client.CartClient;
import com.microservice.order_service.client.ProductClient;
import com.microservice.order_service.domain.OrderStatus;
import com.microservice.order_service.domain.PaymentStatus;
import com.microservice.order_service.domain.dto.CartResponse;
import com.microservice.order_service.domain.dto.CheckoutRequest;
import com.microservice.order_service.kafka.event.StockUpdateEvent;
import com.microservice.order_service.domain.entity.Order;
import com.microservice.order_service.domain.entity.OrderItem;
import com.microservice.order_service.exception.EmptyCartException;
import com.microservice.order_service.kafka.KafkaTopics;
import com.microservice.order_service.repository.OrderRepository;
import com.microservice.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final CartClient cartClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ProductClient prodClient;

    @Override
    public Order checkout(UUID userId, CheckoutRequest request) {
        CartResponse userCart = cartClient.viewCart(userId);

        if(userCart.getItems().isEmpty()) {
            throw new EmptyCartException("Cart is empty. Add items before checkout");
        }

        // Reserve product before saving it
        for(var cartItem : userCart.getItems()) {
            ResponseEntity<String> response = prodClient.reserveStock(cartItem.getProductId(), cartItem.getQuantity());
            if(!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Stock not available for product " + cartItem.getProductId());
            }
        }

        List<OrderItem> orderItems = userCart.getItems().stream()
                .map(cartItem -> OrderItem.builder()
                        .productId(cartItem.getProductId())
                        .orderStatus(OrderStatus.PENDING)
                        .priceAtPurchase(cartItem.getDiscountedPrice())
                        .quantity(cartItem.getQuantity())
                        .purchasedAt(LocalDateTime.now())
                        .build())
                .toList();

        BigDecimal finalPrice = orderItems.stream()
                .map(orderItem -> orderItem.getPriceAtPurchase().multiply(
                        BigDecimal.valueOf(orderItem.getQuantity())
                )).reduce(BigDecimal.ZERO, BigDecimal::add);

        // TODO: Payment-service need to be call (Kafka)
        PaymentStatus status;

        Order order = Order.builder()
                .userId(userId)
                .orderStatus(OrderStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .paymentMethod(request.getPaymentMethod())
                .finalPrice(finalPrice)
                .orderItems(orderItems)
                .shippingAddress(request.getAddressSnap())
                .build();

        order.getOrderItems().forEach(orderItem -> orderItem.setOrder(order));

        Order savedOrder = orderRepo.save(order);

//        savedOrder.getOrderItems().forEach(orderItem -> {
//            StockUpdateEvent event = StockUpdateEvent.builder()
//                    .orderId(savedOrder.getId())
//                    .productId(orderItem.getProductId())
//                    .quantity(orderItem.getQuantity())
//                    .build();
//            kafkaTemplate.send(KafkaTopics.PRODUCT_STOCK_TOPIC, event);
//        });

        // TODO: Empty cart and send confirmation mail

        return savedOrder;
    }
}