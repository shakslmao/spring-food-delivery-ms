package com.devshaks.delivery.customer;

import com.devshaks.delivery.restaurants.RestaurantResponse;
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
        // Call the customer service to create the customer and return the customer's ID in the response
        String customerId = customerService.createCustomer(customerRequest);
        URI location = URI.create("/api/v1/customers/" + customerId);
        return ResponseEntity.created(location).build();
    }

    // Endpoint to update an existing customer
    // Update an existing customer and return 204 No Content
    @PutMapping("/{customer-id}")
    public ResponseEntity<Void> updateCustomer(@PathVariable("customer-id") String customerId, @RequestBody @Valid CustomerRequest customerRequest) {
        // Call the customer service to update the customer
        customerService.updateCustomer(customerId, customerRequest);
        // Return a 200 OK status with no content
        return ResponseEntity.noContent().build();
    }

    // Endpoint to retrieve all customers
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAllCustomers() {
        // Call the customer service to retrieve all customers and return them in the response
        return ResponseEntity.ok(customerService.findAllCustomers());
    }

    // Endpoint to check if a customer exists by ID
    @GetMapping("/{customer-id}")
    public ResponseEntity<Boolean> existingCustomerById(@PathVariable("customer-id") String customerId) {
        // Call the customer service to check if the customer exists and return the result in the response
        if (customerService.existingCustomerById(customerId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint to delete a customer by their ID
    @DeleteMapping("/{customer-id}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable("customer-id") String customerId) {
        customerService.deleteCustomerById(customerId);
        return ResponseEntity.noContent().build();
    }

    // TODO: Implement the following endpoints
    // Add a Restaurant to a customer's favourite list
    @PostMapping("/{customer-id}/favorite-restaurant/{restaurant-id}")
    public ResponseEntity<Void> addRestaurantToFavourites(@PathVariable("customer-id") String customerId, @PathVariable("restaurant-id") String restaurantId) {
        customerService.addRestaurantToFavourites(customerId, restaurantId);
        return ResponseEntity.noContent().build();
    }

    // remove a restaurant from a customers favourite list
    @DeleteMapping("/{customer-id}/favourite-restaurant/{restaurant-id}")
    public ResponseEntity<Void> removeRestaurantFromCustomerFavourites(@PathVariable("customer-id") String customerId, @PathVariable("restaurant-id") String restaurantId) {
       customerService.removeRestaurantFromCustomerFavourites(customerId, restaurantId);
       return ResponseEntity.noContent().build();
    }

    // Retrieve a customer's favorite restaurants
    @GetMapping("/{customer-id}/favourite-restaurants")
    public ResponseEntity<List<RestaurantResponse>> retrieveFavouriteRestaurants(@PathVariable("customer-id") String customerId) {
        return ResponseEntity.ok(customerService.retrieveFavouriteRestaurants(customerId));
    }
}
