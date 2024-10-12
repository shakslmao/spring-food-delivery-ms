package com.devshaks.delivery.restaurant;

import com.devshaks.delivery.cuisine.CuisineRequest;
import com.devshaks.delivery.cuisine.CuisineTypes;
import com.devshaks.delivery.cuisine.CuisineTypesRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RestaurantMapper {
    private final CuisineTypesRepository cuisineTypesRepository;

    public Restaurant mapRestaurantToRequest(@Valid RestaurantRequest restaurantRequest) {
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
                .cuisineTypes(mapCuisineTypes(restaurantRequest.cuisineTypes()))
                .build();
    }

    private List<CuisineTypes> mapCuisineTypes(List<CuisineRequest> cuisineRequests) {
        return cuisineRequests.stream()
                .map(cuisineRequest -> CuisineTypes.builder()
                        .id(cuisineRequest.id())
                        .name(cuisineRequest.name())
                        .description(cuisineRequest.description())
                        .price(cuisineRequest.price())
                        .build())
                .collect(Collectors.toList());
    }


    public RestaurantResponse toRestaurantResponse(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getContactNumber(),
                restaurant.getLocation(),
                restaurant.getDescription(),
                restaurant.getOpeningHours(),
                restaurant.getRating(),
                restaurant.getIsOpen(),
                restaurant.getPriceRange());
    }

    public RestaurantPurchaseResponse toRestaurantPurchaseResponse(Restaurant restaurant, CuisineTypes cuisineTypes) {
        return new RestaurantPurchaseResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getDescription(),
                cuisineTypes);
    }

    public RestaurantFavouriteResponse toFavouriteRestaurantResponse(Restaurant restaurant) {
        return new RestaurantFavouriteResponse(
                restaurant.getId(),
                restaurant.getName());
    }
}
