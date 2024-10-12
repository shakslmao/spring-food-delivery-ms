package com.devshaks.delivery.restaurant;

import com.devshaks.delivery.cuisine.CuisineRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record RestaurantRequest(
        Integer id,

        @NotBlank(message = "Restaurant name is required")
        String name,

        @NotBlank(message = "Restaurant address is required")
        String address,

        @NotBlank(message = "Restaurant contact number is required")
        String contactNumber,

        @NotBlank(message = "Restaurant location is required")
        String location,

        @NotBlank(message = "Restaurant description is required")
        String description,

        @NotBlank(message = "Restaurant opening hours is required")
        String openingHours,

        @NotNull(message = "Restaurant rating is required")
        Double rating,

        @NotNull(message = "Restaurant open status is required")
        Boolean isOpen,

        @NotBlank(message = "Restaurant price range is required")
        String priceRange,

        @NotNull(message = "Restaurant cuisine types is required")
        List<CuisineRequest> cuisineTypes) {
}
