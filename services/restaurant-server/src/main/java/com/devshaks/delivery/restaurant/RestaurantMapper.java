package com.devshaks.delivery.restaurant;

import com.devshaks.delivery.cuisine.CuisineRequest;
import com.devshaks.delivery.cuisine.CuisineTypes;
import com.devshaks.delivery.cuisine.CuisineTypesRepository;
import com.devshaks.delivery.cuisine.CuisineTypesResponse;
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
        Restaurant restaurant = Restaurant.builder()
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
                .build();

        List<CuisineTypes> cuisineTypes = mapCuisineTypes(restaurantRequest.cuisineTypes(), restaurant);
        restaurant.setCuisineTypes(cuisineTypes);

        return restaurant;
    }

    private List<CuisineTypes> mapCuisineTypes(List<CuisineRequest> cuisineRequests, Restaurant restaurant) {
        return cuisineRequests.stream()
                .map(cuisineRequest -> CuisineTypes.builder()
                        .id(cuisineRequest.id())
                        .name(cuisineRequest.name())
                        .description(cuisineRequest.description())
                        .price(cuisineRequest.price())
                        .restaurant(restaurant) // Had to add this line to fix the error
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
                restaurant.getPriceRange(),
                restaurant.getCuisineTypes().stream()
                        .map(cuisineTypes -> new CuisineTypesResponse(
                                cuisineTypes.getId(),
                                cuisineTypes.getName(),
                                cuisineTypes.getDescription(),
                                cuisineTypes.getPrice(),
                                cuisineTypes.getRestaurant().getId()))
                        .collect(Collectors.toList()));
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
