package com.devshaks.delivery.restaurant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record RestaurantRequest(
        Integer id,

        @NotNull(message = "Restaurant name is required") String name,

        @NotNull(message = "Restaurant address is required") String address,

        @NotNull(message = "Restaurant contact number is required") String contactNumber,

        @NotNull(message = "Restaurant location is required") String location,

        @NotNull(message = "Restaurant description is required") String description,

        @NotNull(message = "Restaurant opening hours is required") String openingHours,

        @NotNull(message = "Restaurant rating is required") Double rating,

        @NotNull(message = "Restaurant open status is required") Boolean isOpen,

        @Positive(message = "Restaurant price range is required") String priceRange,

        @NotNull(message = "Restaurant cuisine types is required") List<Integer> cuisineTypesId) {
}
