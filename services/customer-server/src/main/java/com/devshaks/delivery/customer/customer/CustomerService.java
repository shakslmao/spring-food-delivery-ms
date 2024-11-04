package com.devshaks.delivery.customer.customer;

import com.devshaks.delivery.customer.address.Address;
import com.devshaks.delivery.customer.address.AddressRepository;
import com.devshaks.delivery.customer.exceptions.CustomerNotFoundException;
import com.devshaks.delivery.customer.favourites.FavouriteMapper;
import com.devshaks.delivery.customer.favourites.FavouriteRestaurantRepository;
import com.devshaks.delivery.customer.favourites.FavouriteRestaurants;
import com.devshaks.delivery.customer.handlers.UnauthorizedException;

import com.devshaks.delivery.customer.restaurants.RestaurantFeignClient;
import com.devshaks.delivery.customer.restaurants.RestaurantResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final CustomerMapper customerMapper;
    private final RestaurantFeignClient restaurantFeignClient;
    private final FavouriteRestaurantRepository favouriteRestaurantsRepository;
    private final FavouriteMapper favouriteMapper;
    private final FavouriteRestaurantRepository favouriteRestaurantRepository;


    // Method to create a new customer
    public Integer createCustomer(@Valid CustomerRequest customerRequest) {
        try {
            Customer customer = customerMapper.mapCustomerToRequest(customerRequest);
            Address savedAddress = addressRepository.save(customer.getAddress());
            customer.setAddress(savedAddress);
            Customer savedCustomer = customerRepository.save(customer);
            return savedCustomer.getId();
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
                .orElseThrow(() -> new CustomerNotFoundException(
                        "Customer With Id: " + customerRequest.id() + " Not Found"));
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
    public CustomerResponse findCustomerById(Integer customerId) {
        return null;
    }

    // Method to delete a customer by their ID
    public void deleteCustomerById(Integer customerId) {
        // Delete the customer by ID
        customerRepository.deleteById(customerId);
    }

    public void addRestaurantToFavourites(Integer customerId, Integer restaurantId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer with ID: " + customerId + " not found"));
        RestaurantResponse restaurantResponse = restaurantFeignClient.getRestaurantById(restaurantId);
        FavouriteRestaurants favouriteRestaurants = favouriteMapper.mapFavouritesToRestaurantResponse(restaurantResponse);
        customer.getFavouriteRestaurants().add(favouriteRestaurants);
        favouriteRestaurantRepository.save(favouriteRestaurants);
        customerRepository.save(customer);
    }


}