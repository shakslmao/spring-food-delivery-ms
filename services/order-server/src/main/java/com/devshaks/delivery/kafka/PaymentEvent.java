package com.devshaks.delivery.kafka;

import com.devshaks.delivery.order.OrderStatus;
import com.devshaks.delivery.order.PaymentMethod;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentEvent(
        Integer orderId,
        BigDecimal orderAmount,
        UUID orderReference,
        PaymentMethod paymentMethod,
        OrderStatus orderStatus
) {
}
