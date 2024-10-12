package com.devshaks.delivery.cuisine;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CuisineRequest(
        Integer id,
        @NotBlank(message = "Cuisine Name is Required") String name,
        @NotBlank(message = "Cuisine Description is Required") String description,
        @NotNull(message = "Cuisine Price is Required") Double price
) {

}
