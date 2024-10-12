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

    // Injecting CuisineTypesRepository dependency using constructor-based injection (thanks to @AllArgsConstructor).
    private final CuisineTypesRepository cuisineTypesRepository;

    // Method to map a RestaurantRequest object to a Restaurant entity
    public Restaurant mapRestaurantToRequest(@Valid RestaurantRequest restaurantRequest) {
        // Building a Restaurant object from the incoming RestaurantRequest data
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

        // Mapping the list of CuisineTypes associated with the restaurant request
        List<CuisineTypes> cuisineTypes = mapCuisineTypes(restaurantRequest.cuisineTypes(), restaurant);
        restaurant.setCuisineTypes(cuisineTypes); // Set the mapped cuisine types to the restaurant

        return restaurant; // Return the fully built Restaurant object
    }

    // Private helper method to map a list of CuisineRequest objects to CuisineTypes entities
    private List<CuisineTypes> mapCuisineTypes(List<CuisineRequest> cuisineRequests, Restaurant restaurant) {
        return cuisineRequests.stream()
                .map(cuisineRequest -> CuisineTypes.builder()
                        .id(cuisineRequest.id()) // Map cuisine ID
                        .name(cuisineRequest.name()) // Map cuisine name
                        .description(cuisineRequest.description()) // Map cuisine description
                        .price(cuisineRequest.price()) // Map cuisine price
                        .restaurant(restaurant) // Associate the restaurant with the cuisine type (important for bidirectional mapping)
                        .build())
                .collect(Collectors.toList()); // Collect the mapped cuisine types into a list
    }

    // Method to map a Restaurant entity to a RestaurantResponse DTO
    public RestaurantResponse toRestaurantResponse(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(), // Map restaurant ID
                restaurant.getName(), // Map restaurant name
                restaurant.getAddress(), // Map restaurant address
                restaurant.getContactNumber(), // Map restaurant contact number
                restaurant.getLocation(), // Map restaurant location
                restaurant.getDescription(), // Map restaurant description
                restaurant.getOpeningHours(), // Map restaurant opening hours
                restaurant.getRating(), // Map restaurant rating
                restaurant.getIsOpen(), // Map if the restaurant is open
                restaurant.getPriceRange(), // Map restaurant price range
                restaurant.getCuisineTypes().stream() // Map cuisine types associated with the restaurant
                        .map(cuisineTypes -> new CuisineTypesResponse(
                                cuisineTypes.getId(), // Map cuisine type ID
                                cuisineTypes.getName(), // Map cuisine type name
                                cuisineTypes.getDescription(), // Map cuisine type description
                                cuisineTypes.getPrice(), // Map cuisine type price
                                cuisineTypes.getRestaurant().getId())) // Map associated restaurant ID
                        .collect(Collectors.toList())); // Collect the mapped cuisine types into a list
    }

    // Method to map a Restaurant entity and a CuisineTypes entity to a RestaurantPurchaseResponse DTO
    public RestaurantPurchaseResponse toRestaurantPurchaseResponse(Restaurant restaurant, CuisineTypes cuisineTypes) {
        return new RestaurantPurchaseResponse(
                restaurant.getId(), // Map restaurant ID
                restaurant.getName(), // Map restaurant name
                restaurant.getAddress(), // Map restaurant address
                restaurant.getDescription(), // Map restaurant description
                cuisineTypes); // Map the purchased cuisine type
    }

    // Method to map a Restaurant entity to a RestaurantFavouriteResponse DTO (used for marking favorite restaurants)
    public RestaurantFavouriteResponse toFavouriteRestaurantResponse(Restaurant restaurant) {
        return new RestaurantFavouriteResponse(
                restaurant.getId(), // Map restaurant ID
                restaurant.getName()); // Map restaurant name
    }
}
