package com.devshaks.delivery.payments;

import com.devshaks.delivery.customer.CustomerResponse;
import com.devshaks.delivery.order.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.UUID;

@Validated
public record PaymentRequest(
        @NotNull(message = "Order Amount is Required") BigDecimal orderAmount,
        @NotNull(message = "Payment Method is Required") PaymentMethod paymentMethod,
        @NotNull(message = "Order reference is required") UUID orderReference,
        @NotNull(message = "Order reference is required") Integer orderId,
        @NotNull(message = "Order reference is required") CustomerResponse customer
) {
}
