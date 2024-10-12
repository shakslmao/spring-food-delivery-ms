package com.devshaks.delivery.restaurant;

import com.devshaks.delivery.cuisine.CuisineTypes;
import com.devshaks.delivery.cuisine.CuisineTypesResponse;

import java.util.List;

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
        String priceRange,
        List<CuisineTypesResponse> cuisineTypes

        ) {
}
