package com.devshaks.delivery.customer.restaurants;

import java.time.LocalDateTime;

public record RestaurantResponse(
        Integer restaurantId,
        String name,
        LocalDateTime createdAt
        ) {
}
