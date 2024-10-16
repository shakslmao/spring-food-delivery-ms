package com.devshaks.delivery.restaurant;

import com.devshaks.delivery.cuisine.CuisineRequest;
import com.devshaks.delivery.cuisine.CuisineTypes;
import com.devshaks.delivery.cuisine.CuisineTypesRepository;
import com.devshaks.delivery.cuisine.CuisineTypesResponse;
import com.devshaks.delivery.exceptions.CuisineNotFoundException;
import com.devshaks.delivery.exceptions.RestaurantNotFoundException;
import com.devshaks.delivery.exceptions.RestaurantPurchaseException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
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
        if (restaurantRequest.cuisineTypes() == null || restaurantRequest.cuisineTypes().isEmpty()) {
            throw new IllegalArgumentException("At least one cuisine type is required");
        }

        Restaurant restaurant = restaurantMapper.mapRestaurantToRequest(restaurantRequest);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        // Saves the cuisine types to the repository
        cuisineTypesRepository.saveAll(restaurant.getCuisineTypes());
        return savedRestaurant.getId();
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


    /**
     * Searches for restaurants by their IDs.
     *
     * @param restaurantIds
     *            A list of restaurant IDs to search for.
     * @return A list of RestaurantFavouriteResponse objects containing the details of
     *         the found restaurants.
     */
    public List<RestaurantFavouriteResponse> findRestaurantByIds(List<Integer> restaurantIds) {
        List<Restaurant> restaurants = restaurantRepository.findAllById(restaurantIds);
        return restaurants.stream()
                .map(restaurantMapper::toFavouriteRestaurantResponse)
                .collect(Collectors.toList());
    }


    /**
     * Adds a new cuisine to a restaurant.
     *
     * @param restaurantId
     *            The ID of the restaurant to add the cuisine to.
     * @param cuisine
     *            The details of the cuisine to add.
     * @return The newly created CuisineTypes object.
     * @throws RestaurantNotFoundException
     *             if the specified restaurant is not found.
     */
    public CuisineTypes addCuisineToRestaurant(Integer restaurantId, CuisineTypesResponse cuisine) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));

        CuisineTypes newCuisine = CuisineTypes.builder()
                .name(cuisine.name())
                .description(cuisine.description())
                .price(cuisine.price())
                .restaurant(restaurant)
                .build();

        return cuisineTypesRepository.save(newCuisine);
    }


    /**
     * Fetches all cuisine types for a restaurant.
     * @param restaurantId The ID of the restaurant to fetch cuisine types for.
     * @return A list of CuisineTypesResponse objects containing the details of the cuisine types.
     */
    public List<CuisineTypesResponse> findCuisineByRestaurantId(Integer restaurantId) {
        return cuisineTypesRepository.findCuisineTypesByRestaurantId(restaurantId).stream()
                .map(restaurantMapper::toCuisineResponse)
                .collect(Collectors.toList());

    }
    

    /**
     * Deletes a restaurant by its ID.
     * @param restaurantId The ID of the restaurant to delete.
     * @throws RestaurantNotFoundException if the restaurant is not found.
     */
    public void deleteRestaurant(Integer restaurantId) {
        restaurantRepository.deleteById(restaurantId);
    }


    /**
     * Deletes a cuisine from a restaurant.
     * @param restaurantId The ID of the restaurant to delete the cuisine from.
     * @param cuisineId The ID of the cuisine to delete.
     * @throws RestaurantNotFoundException if the restaurant is not found.
     * @throws CuisineNotFoundException if the cuisine is not found.
     */
    public void deleteCuisineFromRestaurant(Integer restaurantId, Integer cuisineId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));

        CuisineTypes cuisine = cuisineTypesRepository.findById(cuisineId)
                .orElseThrow(() -> new CuisineNotFoundException("Cuisine not found"));

        restaurant.getCuisineTypes().remove(cuisine);
        cuisineTypesRepository.delete(cuisine);
    }

    /**
     * Updates the details of a restaurant.
     * @param restaurantId The ID of the restaurant to update.
     * @param restaurantRequest The updated details of the restaurant.
     * @throws RestaurantNotFoundException if the restaurant is not found.
     */
    public void updateRestaurantDetails(Integer restaurantId, RestaurantRequest restaurantRequest) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with ID: " + restaurantId));

        if (!restaurant.getName().equals(restaurantRequest.name())) {
            restaurant.setName(restaurantRequest.name());

        }

        if (!restaurant.getAddress().equals(restaurantRequest.address())) {
            restaurant.setAddress(restaurantRequest.address());
        }

        if (!restaurant.getContactNumber().equals(restaurantRequest.contactNumber())) {
            restaurant.setContactNumber(restaurantRequest.contactNumber());
        }

        if (!restaurant.getLocation().equals(restaurantRequest.location())) {
            restaurant.setLocation(restaurantRequest.location());
        }

        if (!restaurant.getDescription().equals(restaurantRequest.description())) {
            restaurant.setDescription(restaurantRequest.description());
        }

        if (!restaurant.getOpeningHours().equals(restaurantRequest.openingHours())) {
            restaurant.setOpeningHours(restaurantRequest.openingHours());
        }

        if (!restaurant.getRating().equals(restaurantRequest.rating())) {
            restaurant.setRating(restaurantRequest.rating());
        }

        if (!restaurant.getIsOpen().equals(restaurantRequest.isOpen())) {
            restaurant.setIsOpen(restaurantRequest.isOpen());
        }

        if (!restaurant.getPriceRange().equals(restaurantRequest.priceRange())) {
            restaurant.setPriceRange(restaurantRequest.priceRange());

        }

        updateRestaurantCuisines(restaurant, restaurantRequest.cuisineTypes());
        restaurantRepository.save(restaurant);
    }

    private void updateRestaurantCuisines(Restaurant restaurant, List<CuisineRequest> cuisineRequests) {
        Set<CuisineTypes> currentCuisines = new HashSet<>(restaurant.getCuisineTypes());
        Set<CuisineTypes> updatedCuisines = new HashSet<>();

        for (CuisineRequest cuisineRequest : cuisineRequests) {
            CuisineTypes cuisine;

            // Check if the ID is null, meaning this is a new cuisine to be created
            if (cuisineRequest.id() != null) {
                // If the ID is not null, try to fetch the existing cuisine
                cuisine = cuisineTypesRepository.findById(cuisineRequest.id())
                        .orElseThrow(() -> new IllegalArgumentException("Cuisine not found with ID: " + cuisineRequest.id()));
            } else {
                // If the ID is null, create a new cuisine
                cuisine = new CuisineTypes();
                cuisine.setRestaurant(restaurant);  // Set the restaurant
            }

            // Now set the cuisine details from the request (whether new or existing)
            cuisine.setName(cuisineRequest.name());
            cuisine.setDescription(cuisineRequest.description());
            cuisine.setPrice(cuisineRequest.price());

            // Save the new/updated cuisine to the repository
            updatedCuisines.add(cuisineTypesRepository.save(cuisine));
        }

        // Handle removal of old cuisines
        currentCuisines.removeAll(updatedCuisines);
        cuisineTypesRepository.deleteAll(currentCuisines);

        restaurant.setCuisineTypes(new ArrayList<>(updatedCuisines));
    }
}
