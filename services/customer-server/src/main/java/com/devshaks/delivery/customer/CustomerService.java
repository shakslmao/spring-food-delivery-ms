package com.devshaks.delivery.customer;

import com.devshaks.delivery.customer.exceptions.BusinessException;
import com.devshaks.delivery.customer.exceptions.CustomerNotFoundException;
import com.devshaks.delivery.customer.exceptions.RestaurantNotFoundException;
import com.devshaks.delivery.customer.favourites.FavouriteRestaurants;
import com.devshaks.delivery.customer.handlers.UnauthorizedException;
import com.devshaks.delivery.customer.restaurants.RestaurantClient;
import com.devshaks.delivery.customer.restaurants.RestaurantRequest;
import com.devshaks.delivery.customer.restaurants.RestaurantResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final RestaurantClient restaurantClient;

    // Method to create a new customer
    public Integer createCustomer(@Valid CustomerRequest customerRequest) {
        try {
            // Map the incoming customer request to a customer entity and save it to the repository
            var customer = customerRepository.save(customerMapper.mapCustomerToRequest(customerRequest));
            // Return the ID of the newly created customer
            return customer.getId();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                log.error("Unauthorized access to create customer: {}", e.getMessage());
                throw new UnauthorizedException("Unauthorized access to create customer", e);
            }
            throw e;
        } catch (Exception e) {
            log.error("Error creating customer: {}", e.getMessage());
            throw new RuntimeException("Error creating customer", e);
        }
    }

    // Method to update an existing customer
    public void updateCustomer(Integer customerId, @Valid CustomerRequest customerRequest) {
        // Find the customer by ID, throw an exception if not found
        var customer = customerRepository.findById(customerRequest.id())
                .orElseThrow(() -> new CustomerNotFoundException("Customer With Id: " + customerRequest.id() + " Not Found"));
        // Update the customer's details based on the provided request
        updateCustomerCredentials(customer, customerRequest);
        // Save the updated customer entity back to the repository
        customerRepository.save(customer);
    }

    // Helper method to update customer credentials
    private void updateCustomerCredentials(Customer customer, CustomerRequest customerRequest) {
        // Update username if it's provided and not blank
        if (StringUtils.isNotBlank(customerRequest.username())) {
            customer.setUsername(customerRequest.username());
        }

        // Update first name if provided and not blank
        if (StringUtils.isNotBlank(customerRequest.firstName())) {
            customer.setFirstName(customerRequest.firstName());
        }

        // Update last name if provided and not blank
        if (StringUtils.isNotBlank(customerRequest.lastName())) {
            customer.setLastName(customerRequest.lastName());
        }

        // Update email if provided and not blank
        if (StringUtils.isNotBlank(customerRequest.email())) {
            customer.setEmail(customerRequest.email());
        }

        // Update phone number if provided and not blank
        if (StringUtils.isNotBlank(customerRequest.phoneNumber())) {
            customer.setPhoneNumber(customerRequest.phoneNumber());
        }

        // Update address if it's not null
        if (customerRequest.address() != null) {
            customer.setAddress(customerRequest.address());
        }
    }

    // Method to retrieve all customers and map them to a list of responses
    public List<CustomerResponse> findAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::mapCustomerToResponse)
                .toList(); // Map each customer entity to a response and return the list
    }

    // Method to check if a customer exists by their ID
    public Boolean existingCustomerById(Integer customerId) {
        // Return true if the customer exists, false otherwise
        return customerRepository.findById(customerId).isPresent();
    }

    // Method to delete a customer by their ID
    public void deleteCustomerById(Integer customerId) {
        // Delete the customer by ID
        customerRepository.deleteById(customerId);
    }

    // TODO: Implement the following methods
    /**
     * Adds a restaurant to the customer's favorite list.
     *
     * @param customerId        The ID of the customer.
     * @param restaurantId      The ID of the restaurant to add to favorites.
     * @param restaurantRequest The restaurant details from the request body.
     */
    @Transactional
    public void addRestaurantToFavourites(Integer customerId, String restaurantId, RestaurantRequest restaurantRequest) {
        // Fetch the customer by ID, throw an exception if not found
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with ID: " + customerId + " not found"));

        // Fetch the restaurant details from the Restaurant Microservice
        RestaurantResponse restaurantResponse = restaurantClient.getRestaurantsById(restaurantRequest.restaurantId());
        if (restaurantResponse == null) {
            throw new RestaurantNotFoundException("Restaurant with ID: " + restaurantRequest.restaurantId() + " not found");
        }

        // Check if the Restaurant is already in customers favourite list
        boolean isAlreadyFavourited = customer.getFavouriteRestaurants()
                .stream()
                .anyMatch(favouriteRestaurants -> favouriteRestaurants.getId().equals(restaurantId));

        if (isAlreadyFavourited) {
            throw new BusinessException("Restaurant already in favourites");
        }

        // add the restaurant to the customers favourite list
        FavouriteRestaurants favouriteRestaurant = new FavouriteRestaurants(
                restaurantResponse.id(),
                restaurantResponse.name(),
                restaurantResponse.createdAt());
        customer.getFavouriteRestaurants().add(favouriteRestaurant);

        // Save the updated customer entity back to the repository
        customerRepository.save(customer);
    }

    /**
     * Removes a restaurant from the customer's favorite list.
     *
     * @param customerId   The ID of the customer.
     * @param restaurantId The ID of the restaurant to remove from favorites.
     */
    @Transactional
    public void removeRestaurantFromCustomerFavourites(Integer customerId, String restaurantId) {
        // validate input params
        if (customerId == null || restaurantId == null) {
            throw new BusinessException("Customer ID and Restaurant ID must be provided");
        }

        // Fetch the customer by ID, throw an exception if not found
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with ID: " + customerId + " not found"));

        // Check if the Restaurant is in customers favourite list
        boolean removed = customer.getFavouriteRestaurants().removeIf(
                favouriteRestaurant -> favouriteRestaurant.getId().equals(restaurantId)
        );

        if (!removed) {
            throw new BusinessException("Restaurant not found in favourites");
        }

        // Save the updated customer entity back to the repository
        customerRepository.save(customer);
    }

    /**
     * Retrieves a customer's favorite restaurants.
     *
     * @param customerId The ID of the customer.
     * @return A list of RestaurantResponse objects containing the details of the favorite restaurants.
     */
    public List<RestaurantResponse> retrieveFavouriteRestaurants(Integer customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with ID: " + customerId + " not found"));

        List<Integer> favouriteRestaurants = customer.getFavouriteRestaurants()
                .stream()
                .map(FavouriteRestaurants::getId)
                .collect(Collectors.toList());

        if (favouriteRestaurants.isEmpty()) {
            return Collections.emptyList();
        }

        return restaurantClient.getRestaurantsByIds(favouriteRestaurants);
    }
}