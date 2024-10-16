package com.devshaks.delivery.customer;

import com.devshaks.delivery.customer.restaurants.RestaurantRequest;
import com.devshaks.delivery.customer.restaurants.RestaurantResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {
    private final CustomerService customerService;

    // Endpoint to create a new customer
    // Create a new customer and return 201 Created with the location header
    @PostMapping
    public ResponseEntity<Void> createCustomer(@RequestBody @Valid CustomerRequest customerRequest) {
        // Call the customer service to create the customer and return the customer's ID
        // in the response
        Integer customerId = customerService.createCustomer(customerRequest);
        URI location = URI.create("/api/v1/customers/" + customerId);
        return ResponseEntity.created(location).build();
    }

    // Endpoint to update an existing customer
    // Update an existing customer and return 204 No Content
    @PutMapping("/update/{customer-id}")
    public ResponseEntity<Void> updateCustomer(@PathVariable("customer-id") Integer customerId,
            @RequestBody @Valid CustomerRequest customerRequest) {
        // Call the customer service to update the customer
        customerService.updateCustomer(customerId, customerRequest);
        // Return a 200 OK status with no content
        return ResponseEntity.noContent().build();
    }

    // Endpoint to retrieve all customers
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAllCustomers() {
        // Call the customer service to retrieve all customers and return them in the
        // response
        return ResponseEntity.ok(customerService.findAllCustomers());
    }

    // Endpoint to check if a customer exists by ID
    @GetMapping("/{customer-id}")
    public ResponseEntity<CustomerResponse> findCustomerById(@PathVariable("customer-id") Integer customerId) {
        return ResponseEntity.ok(customerService.findCustomerById(customerId));
    }

    // Endpoint to delete a customer by their ID
    @DeleteMapping("/{customer-id}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable("customer-id") Integer customerId) {
        customerService.deleteCustomerById(customerId);
        return ResponseEntity.noContent().build();
    }


/////// Requests from Restaurant Microservice ///////

    // Add a Restaurant to a customer's favourite list
    @PostMapping("/{customer-id}/favourite-restaurant/{restaurant-id}")
    public ResponseEntity<Void> addRestaurantToFavourites(
            @PathVariable("customer-id") Integer customerId,
            @PathVariable("restaurant-id") Integer restaurantId) {
        customerService.addRestaurantToFavourites(customerId, restaurantId);
        URI location = URI.create("/api/v1/customers/" + customerId + "/favourite-restaurant/" + restaurantId);
        return ResponseEntity.created(location).build();
    }

    // remove a restaurant from a customers favourite list
    @DeleteMapping("/{customer-id}/favourite-restaurant/{restaurant-id}")
    public ResponseEntity<Void> removeRestaurantFromCustomerFavourites(@PathVariable("customer-id") Integer customerId,
            @PathVariable("restaurant-id") String restaurantId) {
        customerService.removeRestaurantFromCustomerFavourites(customerId, restaurantId);
        return ResponseEntity.noContent().build();
    }

    // Retrieve a customer's favorite restaurants
    @GetMapping("/{customer-id}/favourite-restaurants")
    public ResponseEntity<List<RestaurantResponse>> retrieveFavouriteRestaurants(
            @PathVariable("customer-id") Integer customerId) {
        return ResponseEntity.ok(customerService.retrieveFavouriteRestaurants(customerId));
    }

}
