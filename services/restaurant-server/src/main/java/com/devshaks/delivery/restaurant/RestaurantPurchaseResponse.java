package com.devshaks.delivery.restaurant;

import com.devshaks.delivery.cuisine.CuisineTypes;

import java.util.List;

public record RestaurantPurchaseResponse(
        Integer restaurantId,
        String restaurantName,
        List<PurchasedItems> items,
        OrderStatus status
) {
}
