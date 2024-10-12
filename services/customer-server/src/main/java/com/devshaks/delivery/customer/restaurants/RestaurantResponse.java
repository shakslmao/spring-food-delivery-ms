package com.devshaks.delivery.customer.restaurants;

import java.time.LocalDateTime;

public record RestaurantResponse(
        Integer id,
        String name,
        LocalDateTime createdAt) {
}
