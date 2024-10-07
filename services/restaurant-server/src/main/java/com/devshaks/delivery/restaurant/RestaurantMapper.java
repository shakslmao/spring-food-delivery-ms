package com.devshaks.delivery.restaurant;

import com.devshaks.delivery.cuisine.CuisineTypes;
import com.devshaks.delivery.cuisine.CuisineTypesRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RestaurantMapper {
    private final CuisineTypesRepository cuisineTypesRepository;

    public Restaurant mapRestaurantToRequest(@Valid RestaurantRequest restaurantRequest) {
        List<CuisineTypes> cuisineTypes = cuisineTypesRepository.findAllById(restaurantRequest.cuisineTypesId());
        return Restaurant.builder()
                .id(restaurantRequest.id())
                .name(restaurantRequest.name())
                .address(restaurantRequest.address())
                .contactNumber(restaurantRequest.contactNumber())
                .location(restaurantRequest.location())
                .description(restaurantRequest.description())
                .openingHours(restaurantRequest.openingHours())
                .rating(restaurantRequest.rating())
                .isOpen(restaurantRequest.isOpen())
                .priceRange(restaurantRequest.priceRange())
                .cuisineTypes(cuisineTypes)
                .build();

    }
}
