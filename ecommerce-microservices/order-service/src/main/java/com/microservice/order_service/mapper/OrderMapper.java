package com.microservice.order_service.mapper;

import com.microservice.order_service.domain.AddressSnap;
import com.microservice.order_service.domain.dto.AddressDto;
import com.microservice.order_service.domain.dto.OrderItemResponseDto;
import com.microservice.order_service.domain.dto.OrderResponseDto;
import com.microservice.order_service.domain.entity.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public OrderResponseDto toResponse(Order order) {
        List<OrderItemResponseDto> items = order.getOrderItems().stream()
                .map(orderItem -> OrderItemResponseDto.builder()
                        .productName(orderItem.getProductName())
                        .productId(orderItem.getProductId())
                        .priceAtPurchase(orderItem.getPriceAtPurchase())
                        .quantity(orderItem.getQuantity())
                        .build())
                .toList();

        AddressSnap address = order.getShippingAddress();
        AddressDto addressDto = AddressDto.builder()
                .area(address.getArea())
                .city(address.getCity())
                .state(address.getState())
                .postalCode(address.getPostalCode())
                .houseNo(address.getHouseNo())
                .streetNo(address.getStreetNo())
                .country(address.getCountry())
                .build();

        return OrderResponseDto.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus(order.getPaymentStatus())
                .finalPrice(order.getFinalPrice())
                .createdAt(order.getCreatedAt())
                .shippingAddress(addressDto)
                .orderItems(items)
                .updatedAt(order.getUpdatedAt())
                .build();
    }

}
