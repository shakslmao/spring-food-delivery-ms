package com.devshaks.delivery.restaurant;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public record RestaurantPurchaseRequest(
        @NotNull(message = "Restaurant ID is required")
        Integer restaurantId,
        @NotNull(message = "Cuisine is required")
        List<CuisinePurchaseRequest> items
) {
}
