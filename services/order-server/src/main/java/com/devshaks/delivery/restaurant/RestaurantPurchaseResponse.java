package com.devshaks.delivery.restaurant;

import com.devshaks.delivery.order.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record RestaurantPurchaseResponse(
        UUID orderReference,
        Integer restaurantId,
        String restaurantName,
        List<PurchasedItems> items,
        BigDecimal orderAmount,
        OrderStatus orderStatus
) {
}
