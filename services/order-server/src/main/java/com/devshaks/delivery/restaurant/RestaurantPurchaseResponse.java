package com.devshaks.delivery.restaurant;

import com.devshaks.delivery.order.OrderStatus;

import java.math.BigDecimal;
import java.util.List;


public record RestaurantPurchaseResponse(
        Integer restaurantId,
        String restaurantName,
        List<PurchasedItems> items,
        BigDecimal orderAmount,
        OrderStatus orderStatus
) {
}
