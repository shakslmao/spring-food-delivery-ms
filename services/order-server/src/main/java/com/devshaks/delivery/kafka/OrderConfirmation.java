package com.devshaks.delivery.kafka;

import com.devshaks.delivery.customer.CustomerResponse;
import com.devshaks.delivery.order.PaymentMethod;
import com.devshaks.delivery.restaurant.RestaurantPurchaseResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderConfirmation(
        UUID orderReference,
        BigDecimal orderAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<RestaurantPurchaseResponse> purchaseResponse
) {
}
