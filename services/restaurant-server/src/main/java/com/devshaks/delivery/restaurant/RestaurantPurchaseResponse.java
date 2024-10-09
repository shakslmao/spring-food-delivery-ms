package com.devshaks.delivery.restaurant;

import com.devshaks.delivery.cuisine.CuisineTypes;

public record RestaurantPurchaseResponse(
        Integer restaurantId,
        String restaurantName,
        String address,
        String description,
        CuisineTypes cuisineType
) {
}
