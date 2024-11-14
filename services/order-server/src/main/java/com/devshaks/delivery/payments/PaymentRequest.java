package com.devshaks.delivery.payments;

import com.devshaks.delivery.customer.CustomerResponse;
import com.devshaks.delivery.order.PaymentMethod;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequest(
        BigDecimal orderAmount,
        PaymentMethod paymentMethod,
        UUID orderReference,
        Integer orderId,
        CustomerResponse customer
) {
}
