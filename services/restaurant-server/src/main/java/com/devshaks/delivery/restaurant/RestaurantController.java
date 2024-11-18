package com.devshaks.delivery.restaurant;

import com.devshaks.delivery.cuisine.CuisineTypes;
import com.devshaks.delivery.cuisine.CuisineTypesResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    // Injects the RestaurantService to handle business logic
    private final RestaurantService restaurantService;

    /**
     * Endpoint to create a new restaurant.
     *
     * @param restaurantRequest
     *            A valid RestaurantRequest object containing the details of the
     *            restaurant to be created.
     * @return A ResponseEntity with a URI location of the newly created restaurant
     *         and a 201 CREATED status.
     */
    @PostMapping
    public ResponseEntity<Void> createRestaurant(@RequestBody @Valid RestaurantRequest restaurantRequest) {
        // Calls the service to create a new restaurant and retrieve its ID
        Integer restaurantId = restaurantService.createRestaurant(restaurantRequest);

        // Creates a URI that points to the newly created restaurant
        URI location = URI.create("/api/v1/restaurants/" + restaurantId);

        // Returns a response with the created status and the restaurant location in the
        // header
        return ResponseEntity.created(location).build();
    }

    /**
     * Endpoint to fetch all cuisines of a restaurant.
     * 
     * @param restaurantId
     *            The ID of the restaurant to retrieve cuisines from.
     * @return A ResponseEntity containing a list of CuisineTypesResponse objects
     *         and a 200 OK status.
     */
    @GetMapping("/{restaurantId}/cuisine")
    public ResponseEntity<List<CuisineTypesResponse>> findCuisineByRestaurantId(
            @PathVariable("restaurantId") Integer restaurantId) {
        return ResponseEntity.ok(restaurantService.findCuisineByRestaurantId(restaurantId));
    }

    /**
     * Endpoint to delete a restaurant by its ID.
     * 
     * @param restaurantId
     *            The ID of the restaurant to delete.
     * @return A ResponseEntity with a 204 NO CONTENT status.
     */
    @DeleteMapping("/delete/{restaurantId}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable("restaurantId") Integer restaurantId) {
        restaurantService.deleteRestaurant(restaurantId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint to delete a cuisine from a restaurant.
     * 
     * @param restaurantId
     *            The ID of the restaurant to delete the cuisine from.
     * @param cuisineId
     *            The ID of the cuisine to delete.
     * @return A ResponseEntity with a 204 NO CONTENT status.
     */
    @DeleteMapping("/delete/{restaurantId}/cuisine/{cuisineId}")
    public ResponseEntity<Void> deleteCuisineFromRestaurant(
            @PathVariable("restaurantId") Integer restaurantId,
            @PathVariable("cuisineId") Integer cuisineId) {
        restaurantService.deleteCuisineFromRestaurant(restaurantId, cuisineId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint to update the details of a restaurant.
     *
     * @param restaurantId
     *            The ID of the restaurant to update.
     * @param restaurantRequest
     *            A valid RestaurantRequest object containing the updated details of
     *            the restaurant.
     * @return A ResponseEntity with a 204 NO CONTENT status.
     */
    @PutMapping("/update/{restaurantId}")
    public ResponseEntity<Void> updateRestaurantDetails(@PathVariable("restaurantId") Integer restaurantId,
            @RequestBody @Valid RestaurantRequest restaurantRequest) {
        restaurantService.updateRestaurantDetails(restaurantId, restaurantRequest);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint to fetch details of a restaurant by its ID.
     *
     * @param restaurantId
     *            The ID of the restaurant to retrieve.
     * @return A ResponseEntity containing the RestaurantResponse object and a 200
     *         OK status if the restaurant is found.
     *         Returns a 404 Not Found status if the restaurant does not exist.
     */
    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponse> findRestaurantById(@PathVariable("restaurantId") Integer restaurantId) {
        return ResponseEntity.ok(restaurantService.findRestaurantById(restaurantId));
    }

    /**
     * Endpoint to fetch details of all restaurants.
     *
     * @return A ResponseEntity containing a list of RestaurantResponse objects and
     *         a
     *         200 OK status.
     */
    @GetMapping()
    public ResponseEntity<List<RestaurantResponse>> findAllRestaurants() {
        return ResponseEntity.ok(restaurantService.findAllRestaurants());

    }

    /**
     * Endpoint to search for restaurants by their IDs.
     *
     * @param restaurantIds
     *            A list of restaurant IDs to search for.
     * @return A ResponseEntity containing a list of RestaurantFavouriteResponse
     *         objects and a 200 OK status.
     */
    @PostMapping("/search/by-ids")
    public ResponseEntity<List<RestaurantFavouriteResponse>> findRestaurantByIds(
            @RequestBody List<Integer> restaurantIds) {
        return ResponseEntity.ok(restaurantService.findRestaurantByIds(restaurantIds));
    }

    /**
     * Endpoint to add a new cuisine to a restaurant.
     *
     * @param restaurantId
     *            The ID of the restaurant to add the cuisine to.
     * @param cuisine
     *            A valid CuisineTypesResponse object containing the details of the
     *            cuisine to be added.
     * @return A ResponseEntity containing the newly created CuisineTypes object and
     *         a 200 OK status.
     */
    @PostMapping("/restaurants/{restaurantId}/cuisine")
    public ResponseEntity<CuisineTypes> addCuisineToRestaurant(@PathVariable("restaurantId") Integer restaurantId,
            @RequestBody CuisineTypesResponse cuisine) {
        // Calls the service to add a new cuisine to the restaurant
        CuisineTypes newCuisine = restaurantService.addCuisineToRestaurant(restaurantId, cuisine);
        // Returns a response with the newly created cuisine and a 201 CREATED status
        return ResponseEntity.ok(newCuisine);
    }


    @PostMapping("/{restaurantId}/purchase")
    public ResponseEntity<RestaurantPurchaseResponse> processPurchase(
            @PathVariable("restaurantId") Integer restaurantId,
            @RequestBody @Valid RestaurantPurchaseRequest purchaseRequest) {

        log.info("Received Purchase Request for Restaurant ID: {}", restaurantId);
        log.debug("Purchase request details: {}", purchaseRequest);

        // Validate and process the request
        RestaurantPurchaseResponse response = restaurantService.processPurchase(purchaseRequest, restaurantId);

        log.info("Successfully processed purchase request for Restaurant ID: {}", restaurantId);
        log.debug("Response details: {}", response);

        // Return HTTP 200 OK with the response body
        return ResponseEntity.ok(response);
    }



}
