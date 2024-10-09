package com.devshaks.delivery.restaurant;

import jakarta.validation.constraints.NotNull;

public record RestaurantPurchaseRequest(
        @NotNull(message = "Restaurant is Required") Integer restaurantId,
        @NotNull(message = "Cuisine is Required") Integer cuisineId

) {
}
