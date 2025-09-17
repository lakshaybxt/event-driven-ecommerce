package com.microservice.order_service.service.implementation;

import com.microservice.order_service.client.CartClient;
import com.microservice.order_service.domain.OrderStatus;
import com.microservice.order_service.domain.PaymentStatus;
import com.microservice.order_service.domain.dto.CartResponse;
import com.microservice.order_service.domain.dto.CheckoutRequest;
import com.microservice.order_service.domain.entity.Order;
import com.microservice.order_service.domain.entity.OrderItem;
import com.microservice.order_service.exception.EmptyCartException;
import com.microservice.order_service.repository.OrderRepository;
import com.microservice.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
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

    @Override
    public Order checkout(UUID userId, CheckoutRequest request) {
        CartResponse userCart = cartClient.viewCart(userId);

        if(userCart.getItems().isEmpty()) {
            throw new EmptyCartException("Cart is empty. Add items before checkout");
        }

        // TODO: Kafka sends message to product to reduce the quantity

        List<OrderItem> orderItems = userCart.getItems().stream()
                .map(cartItem -> OrderItem.builder()
                        .productId(cartItem.getProductId())
                        .priceAtPurchase(cartItem.getDiscountedPrice())
                        .quantity(cartItem.getQuantity())
                        .orderStatus(OrderStatus.PLACED)
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
                .orderStatus(OrderStatus.PLACED)
                .paymentStatus(status)
                .paymentMethod(request.getPaymentMethod())
                .finalPrice(finalPrice)
                .orderItems(orderItems)
                .shippingAddress(addressSnap)
                .build();

        order.getOrderItems().forEach(orderItem -> orderItem.setOrder(order));

        Order savedOrder = orderRepo.save(order);

        //TODO: Call Cart-Service through kafka to empty the cart

        //TODO: send confirmation mail

        return savedOrder;
    }
}