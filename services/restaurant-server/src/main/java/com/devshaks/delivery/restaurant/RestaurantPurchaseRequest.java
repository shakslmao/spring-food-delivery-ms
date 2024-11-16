package com.devshaks.delivery.restaurant;

import com.devshaks.delivery.cuisine.CuisineRequest;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RestaurantPurchaseRequest(
        @NotNull(message = "Restaurant is Required") Integer restaurantId,
        @NotNull(message = "Cuisine is Required") List<CuisinePurchaseRequest> items
) {
}
