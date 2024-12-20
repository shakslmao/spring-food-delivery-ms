package com.devshaks.delivery.order;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class OrderMapper {
    public Order mapToOrder(@Valid OrderRequest orderRequest) {
        return Order.builder()
                .id(orderRequest.id())
                .paymentMethod(orderRequest.paymentMethod())
                .customerId(orderRequest.customerId())
                .build();
    }

}
