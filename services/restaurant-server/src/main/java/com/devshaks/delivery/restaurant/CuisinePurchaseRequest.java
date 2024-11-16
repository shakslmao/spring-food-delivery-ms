package com.devshaks.delivery.restaurant;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CuisinePurchaseRequest(
        @NotNull(message = "Cuisine must be selected")
        Integer cuisineId,

        @Positive(message = "Quantity must be positive")
        int quantity
) {
}
