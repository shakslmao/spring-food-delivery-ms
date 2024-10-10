package com.devshaks.delivery.restaurants;

import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotNull;

@Validated
public record RestaurantRequest(
        @NotNull(message = "Restaurant Is Required") Integer restaurantId) {
}
