package com.devshaks.delivery.payments;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequest(
        Integer id,
        BigDecimal paymentAmount,
        PaymentMethod paymentMethod,
        UUID orderReference,
        Customer customer
) {
}
