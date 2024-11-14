package com.devshaks.delivery.cuisine;

public record CuisineTypesResponse(
        Integer id,
        String name,
        String description,
        Double price,
        Integer restaurantId) {
}
