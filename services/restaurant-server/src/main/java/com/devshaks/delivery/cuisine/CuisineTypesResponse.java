package com.devshaks.delivery.cuisine;

import com.devshaks.delivery.restaurant.Restaurant;

public record CuisineTypesResponse(
        Integer id,
        String name,
        String description,
        Double price,
        Integer restaurantId
) {
}
