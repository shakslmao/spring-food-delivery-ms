package com.devshaks.delivery.restaurant;

import com.devshaks.delivery.cuisine.CuisineRequest;
import com.devshaks.delivery.cuisine.CuisineTypes;
import com.devshaks.delivery.cuisine.CuisineTypesRepository;
import com.devshaks.delivery.cuisine.CuisineTypesResponse;
import com.devshaks.delivery.exceptions.CuisineNotFoundException;
import com.devshaks.delivery.exceptions.RestaurantNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
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
     * Finds a restaurant by its ID.
     *
     * @param restaurantId,
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


    public RestaurantPurchaseResponse processPurchase(@Valid RestaurantPurchaseRequest purchaseRequest, Integer restaurantId) {
        log.info("Starting purchase processing for Restaurant ID: {}", restaurantId);

        // Step 1: Validate restaurant
        log.info("Validating restaurant with ID: {}", restaurantId);
        var restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with ID: " + restaurantId));
        log.debug("Restaurant details: {}", restaurant);

        if (!restaurant.getId().equals(purchaseRequest.restaurantId())) {
            log.error("Mismatch between path restaurant ID: {} and request restaurant ID: {}", restaurantId, purchaseRequest.restaurantId());
            throw new IllegalArgumentException("Restaurant ID does not match the purchase request");
        }
        log.info("Restaurant validation successful");

        // Step 2: Process purchase items
        log.info("Processing purchase items for Restaurant ID: {}", restaurantId);
        List<PurchasedItems> purchasedItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CuisinePurchaseRequest item : purchaseRequest.items()) {
            log.debug("Processing item: {}", item);

            // Fetch and validate cuisine
            var cuisine = cuisineTypesRepository.findById(item.cuisineId())
                    .orElseThrow(() -> new CuisineNotFoundException("Cuisine not found with ID: " + item.cuisineId()));
            log.debug("Cuisine details: {}", cuisine);

            // Calculate total for the item
            BigDecimal itemTotal = cuisine.getPrice().multiply(BigDecimal.valueOf(item.quantity()));
            totalAmount = totalAmount.add(itemTotal);
            log.info("Item processed: {} - Quantity: {}, Unit Price: {}, Total Price: {}",
                    cuisine.getName(), item.quantity(), cuisine.getPrice(), itemTotal);

            // Add to purchased items
            purchasedItems.add(new PurchasedItems(
                    item.cuisineId(),
                    cuisine.getName(),
                    item.quantity(),
                    cuisine.getPrice(),
                    itemTotal));
        }
        log.info("All purchase items processed successfully");

        // Step 3: Save restaurant data (if applicable)
        log.info("Saving restaurant information (if needed) for Restaurant ID: {}", restaurantId);
        restaurantRepository.save(restaurant);
        log.info("Restaurant data saved successfully");

        // Step 4: Build response
        log.info("Building response for purchase");
        var response = new RestaurantPurchaseResponse(
                restaurant.getId(),
                restaurant.getName(),
                purchasedItems,
                totalAmount,
                OrderStatus.CONFIRMED
        );
        log.debug("Response details: {}", response);

        log.info("Purchase processing completed for Restaurant ID: {}", restaurantId);
        return response;
    }





}
