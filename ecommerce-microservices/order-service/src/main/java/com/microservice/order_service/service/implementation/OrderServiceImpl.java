package com.microservice.order_service.service.implementation;

import com.microservice.order_service.client.CartClient;
import com.microservice.order_service.client.ProductClient;
import com.microservice.order_service.domain.OrderStatus;
import com.microservice.order_service.domain.PaymentStatus;
import com.microservice.order_service.domain.dto.CartResponse;
import com.microservice.order_service.domain.dto.CheckoutRequest;
import com.microservice.order_service.domain.entity.Order;
import com.microservice.order_service.domain.entity.OrderItem;
import com.microservice.order_service.exception.AccessDeniedException;
import com.microservice.order_service.exception.EmptyCartException;
import com.microservice.order_service.exception.OrderCancellationNotAllowedException;
import com.microservice.order_service.kafka.event.OrderEvent;
import com.microservice.order_service.repository.OrderRepository;
import com.microservice.order_service.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final CartClient cartClient;
    private final ProductClient prodClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

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
                        .productName(cartItem.getProductName())
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


        // TODO: Payment-service need to be call (Kafka)
        OrderEvent event = OrderEvent.builder()
                .userId(userId)
                .orderId(savedOrder.getId())
                .amount(finalPrice)
                .currency("INR")
                .build();

        kafkaTemplate.send("order-events", event); // Payment-service

        // TODO: Empty cart and send confirmation mail

        return savedOrder;
    }

    @Override
    public Order getOrderById(UUID userId, UUID orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        if (!order.getUserId().equals(userId)) {
            throw new AccessDeniedException("You are not allowed to access this order.");
        }

        return order;
    }

    @Transactional
    @Override
    public void cancelOrder(UUID userId, UUID orderId) {
        Order order = getOrderById(userId, orderId);

        if (!(order.getOrderStatus() == OrderStatus.PENDING ||
                order.getOrderStatus() == OrderStatus.PLACED)) {
            throw new OrderCancellationNotAllowedException(
                    "Order " + orderId + " cannot be cancelled because it is already " + order.getOrderStatus()
            );
        }

        order.setOrderStatus(OrderStatus.CANCELLED);

        for (OrderItem item : order.getOrderItems()) {
            ResponseEntity<String> response = prodClient.unreserveStock(item.getProductId(), item.getQuantity());

            if (!response.getStatusCode().is2xxSuccessful()) {
                // rollback will happen automatically because of @Transactional
                throw new RuntimeException(
                        "Failed to unreserve stock for Product " + item.getProductId() + " in Order " + order.getId()
                );
            }
            log.info("Unreserved stock for Product {} in Order {}", item.getProductId(), order.getId());
        }

        orderRepo.save(order);

    }

    @Override
    public Order updateOrderStatus(UUID orderId, OrderStatus status) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        order.setOrderStatus(status);

        Order updatedOrder = orderRepo.save(order);

        // TODO: Send mail to customer that order status changed
        // mailService.sendOrderStatusUpdate(updatedOrder);

        return updatedOrder;
    }

    @Override
    public List<Order> getUserOrders(UUID userId) {
        return orderRepo.findAllByUserId(userId);
    }

    @Override
    public boolean existsByProductId(UUID productId) {
        return orderRepo.existsByOrderItemsProductId(productId);
    }

}