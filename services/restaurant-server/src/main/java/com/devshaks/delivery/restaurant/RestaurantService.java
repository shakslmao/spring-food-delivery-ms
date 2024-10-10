package com.devshaks.delivery.restaurant;

import com.devshaks.delivery.cuisine.CuisineTypes;
import com.devshaks.delivery.cuisine.CuisineTypesRepository;
import com.devshaks.delivery.exceptions.CuisineNotFoundException;
import com.devshaks.delivery.exceptions.RestaurantPurchaseException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    // Injects required dependencies for restaurant mapping, repository access, and
    // cuisine management
    private final RestaurantMapper restaurantMapper;
    private final RestaurantRepository restaurantRepository;
    private final CuisineTypesRepository cuisineTypesRepository;

    /**
     * Creates a new restaurant entity in the system based on the provided request.
     *
     * @param restaurantRequest
     *            A valid RestaurantRequest containing restaurant details.
     * @return The unique identifier (ID) of the newly created restaurant.
     */
    public Integer createRestaurant(@Valid RestaurantRequest restaurantRequest) {
        // Maps the request data to a Restaurant entity and saves it to the repository
        var restaurant = restaurantMapper.mapRestaurantToRequest(restaurantRequest);
        return restaurantRepository.save(restaurant).getId(); // Returns the generated ID
    }

    /**
     * Handles the purchase of cuisines from a specific restaurant.
     *
     * @param restaurantPurchaseRequests
     *            A list of requests specifying the restaurant and cuisine(s) to be
     *            purchased.
     * @param restaurantId
     *            The ID of the restaurant where the purchase is being made.
     * @return A list of RestaurantPurchaseResponse objects containing the
     *         restaurant and cuisine details.
     * @throws RestaurantPurchaseException
     *             If the requests are not for the same restaurant or the restaurant
     *             is not found.
     * @throws CuisineNotFoundException
     *             If any requested cuisine does not exist or does not belong to the
     *             specified restaurant.
     */
    public List<RestaurantPurchaseResponse> purchaseDelivery(List<RestaurantPurchaseRequest> restaurantPurchaseRequests,
            Integer restaurantId) {

        // Validates that all the purchase requests are for the same restaurant
        if (restaurantPurchaseRequests.stream().anyMatch(request -> !request.restaurantId().equals(restaurantId))) {
            throw new RestaurantPurchaseException("All purchase requests must be for the same restaurant");
        }

        // Attempts to fetch the restaurant from the repository, throws an exception if
        // not found
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantPurchaseException("Restaurant not found"));

        // Collects all requested cuisine IDs from the purchase requests
        List<Integer> cuisineIds = restaurantPurchaseRequests.stream()
                .map(RestaurantPurchaseRequest::cuisineId)
                .collect(Collectors.toList());

        // Retrieves all cuisines matching the requested IDs from the repository
        List<CuisineTypes> cuisines = cuisineTypesRepository.findAllById(cuisineIds);

        // Checks if any requested cuisines are missing from the fetched list
        if (cuisines.size() != cuisineIds.size()) {
            // Identifies which cuisine IDs were not found
            List<Integer> foundCuisines = cuisines.stream()
                    .map(CuisineTypes::getId)
                    .collect(Collectors.toList());
            List<Integer> missingCuisines = cuisineIds.stream()
                    .filter(id -> !foundCuisines.contains(id))
                    .collect(Collectors.toList());

            // Throws an exception indicating the missing cuisine(s)
            throw new CuisineNotFoundException("Cuisine(s) with ID(s) " + missingCuisines + " not found");
        }

        // Ensures that each fetched cuisine belongs to the specified restaurant
        cuisines.forEach(cuisine -> {
            if (!cuisine.getRestaurant().getId().equals(restaurantId)) {
                throw new CuisineNotFoundException("Cuisine type does not belong to the restaurant");
            }
        });

        // Maps the fetched cuisines to their respective RestaurantPurchaseResponse
        // objects
        return cuisines.stream()
                .map(cuisineTypes -> restaurantMapper.toRestaurantPurchaseResponse(restaurant, cuisineTypes))
                .collect(Collectors.toList());
    }

    /**
     * Finds a restaurant by its ID.
     *
     * @param restaurantId
     *            The ID of the restaurant to find.
     * @return A RestaurantResponse containing the restaurant's details.
     * @throws EntityNotFoundException
     *             if the restaurant is not found.
     */
    public RestaurantResponse findRestaurantById(Integer restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .map(restaurantMapper::toRestaurantResponse)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));
    }

    /**
     * Fetches all restaurants in the system.
     *
     * @return A list of RestaurantResponse objects containing the details of all
     *         restaurants.
     */
    public List<RestaurantResponse> findAllRestaurants() {
        return restaurantRepository.findAll().stream().map(restaurantMapper::toRestaurantResponse)
                .collect(Collectors.toList());

    }
}
