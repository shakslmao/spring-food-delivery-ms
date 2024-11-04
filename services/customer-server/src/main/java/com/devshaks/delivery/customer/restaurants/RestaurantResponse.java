package com.devshaks.delivery.customer.restaurants;

import java.time.LocalDateTime;
public record RestaurantResponse(
        Integer id,
        String name,
        String address,
        String contactNumber,
        String location,
        String description,
        String openingHours,
        Double rating,
        Boolean isOpen,
        String priceRange
        ) {
}
