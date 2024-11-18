package com.devshaks.delivery.cuisine;

import java.math.BigDecimal;

public record CuisineTypesResponse(
        Integer id,
        String name,
        String description,
        BigDecimal price,
        Integer restaurantId) {
}
