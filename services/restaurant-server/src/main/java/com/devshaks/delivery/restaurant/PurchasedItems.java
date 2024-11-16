package com.devshaks.delivery.restaurant;

import java.math.BigDecimal;

public record PurchasedItems(
        Integer cuisineId,
        String name,
        int quantity,
        BigDecimal price,
        BigDecimal totalPrice
) {
}
