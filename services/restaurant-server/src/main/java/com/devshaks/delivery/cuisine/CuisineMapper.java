package com.devshaks.delivery.cuisine;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CuisineMapper {
    public CuisineTypes mapCuisineToRestaurantRequest(CuisineRequest cuisineRequest) {
        return CuisineTypes.builder()
                .id(cuisineRequest.id())
                .name(cuisineRequest.name())
                .description(cuisineRequest.description())
                .price(cuisineRequest.price())
                .build();
    }
}
