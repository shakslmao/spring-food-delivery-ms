package com.devshaks.delivery.restaurant;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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
     * Endpoint to handle a purchase request from a specific restaurant.
     *
     * @param restaurantId
     *            The ID of the restaurant where the purchase is being made.
     * @param purchaseRequests
     *            A list of valid RestaurantPurchaseRequest objects specifying the
     *            cuisines being purchased.
     * @return A ResponseEntity containing a list of RestaurantPurchaseResponse
     *         objects and a 201 CREATED status.
     */
    @PostMapping("/{restaurantId}/purchase")
    public ResponseEntity<List<RestaurantPurchaseResponse>> purchaseDelivery(
            @PathVariable("restaurantId") Integer restaurantId,
            @RequestBody @Valid List<RestaurantPurchaseRequest> purchaseRequests) {
        // Calls the service to process the purchase and get a list of responses
        List<RestaurantPurchaseResponse> responses = restaurantService.purchaseDelivery(purchaseRequests, restaurantId);

        // Returns a response with the purchase details and the created status
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
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

    @PostMapping("/search/by-ids")
    public ResponseEntity<List<RestaurantFavouriteResponse>> findRestaurantByIds(
            @RequestBody List<Integer> restaurantIds) {
        return ResponseEntity.ok(restaurantService.findRestaurantByIds(restaurantIds));
    }
}
