package com.devshaks.delivery.customer;

import com.devshaks.delivery.restaurants.RestaurantResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {
    private final CustomerService customerService;

    // Endpoint to create a new customer
    @PostMapping
    public ResponseEntity<String> createCustomer(@RequestBody @Valid CustomerRequest customerRequest) {
        // Call the customer service to create the customer and return the customer's ID in the response
        return ResponseEntity.ok(customerService.createCustomer(customerRequest));
    }

    // Endpoint to update an existing customer
    @PutMapping
    public ResponseEntity<Void> updateCustomer(@RequestBody @Valid CustomerRequest customerRequest) {
        // Call the customer service to update the customer
        customerService.updateCustomer(customerRequest);
        // Return a 200 OK status with no content
        return ResponseEntity.ok().build();
    }

    // Endpoint to retrieve all customers
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAllCustomers() {
        // Call the customer service to retrieve all customers and return them in the response
        return ResponseEntity.ok(customerService.findAllCustomers());
    }

    // Endpoint to check if a customer exists by ID
    @GetMapping("/existing-customer/{customer-id}")
    public ResponseEntity<Boolean> existingCustomerById(@PathVariable("customer-id") String customerId) {
        // Call the customer service to check if the customer exists and return the result in the response
        return ResponseEntity.ok(customerService.existingCustomerById(customerId));
    }

    // Endpoint to find a customer by their ID
    @GetMapping("/{customer-id}")
    public ResponseEntity<CustomerResponse> findCustomerById(@PathVariable("customer-id") String customerId) { 
        return ResponseEntity.ok(customerService.findCustomerById(customerId));   
    }

    // Endpoint to delete a customer by their ID
    @DeleteMapping("/{customer-id}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable("customer-id") String customerId) {
        customerService.deleteCustomerById(customerId);
        return ResponseEntity.accepted().build();
    }

    // TODO:
    // Add a Restaurant to a customers favourite list
    @PostMapping("/{customer-id}/favourite-restaurant/")
    public ResponseEntity<String> addRestaurantToFavourites(@PathVariable("customer-id")  String customerId) {
        return null;
    }

    // remove a restaurant from a customers favourite list
    @DeleteMapping("/{customer-id}/favourite-restaurant/{restaurant-id}")
    public ResponseEntity<String> removeRestaurantFromCustomerFavourites(@PathVariable("customer-id") String customerId, @PathVariable("restaurant-id") String restaurantId) {
        return null;
    }

    @GetMapping("/{customer-id}/favourite-restaurants")
    public ResponseEntity<List<RestaurantResponse>> retrieveFavouriteRestaurants(@PathVariable("customer-id") String customerId) {
        return null;
    }
}
