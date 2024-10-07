package com.devshaks.delivery.customer;

import com.devshaks.delivery.customer.exceptions.CustomerNotFoundException;
import com.devshaks.delivery.customer.handlers.UnauthorizedException;
import com.devshaks.delivery.restaurants.RestaurantResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    // Method to create a new customer
    public String createCustomer(@Valid CustomerRequest customerRequest) {
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
    public void updateCustomer(String customerId, @Valid CustomerRequest customerRequest) {
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
    public Boolean existingCustomerById(String customerId) {
        // Return true if the customer exists, false otherwise
        return customerRepository.findById(customerId).isPresent();
    }

    // Method to find a customer by their ID
    public CustomerResponse findCustomerById(String customerId) {
        // Returns the customer Response if the customer exits, if not it throws an exception
        return customerRepository.findById(customerId).map(customerMapper::mapCustomerToResponse).orElseThrow(() -> new CustomerNotFoundException("Customer With Id: " + customerId + " Not Found"));
    }

    // Method to delete a customer by their ID
    public void deleteCustomerById(String customerId) {
        // Delete the customer by ID
        customerRepository.deleteById(customerId);
    }

    // TODO: Implement the following methods

    public void addRestaurantToFavourites(String customerId, String restaurantId) {

    }

    public void removeRestaurantFromCustomerFavourites(String customerId, String restaurantId) {
    }

    public List<RestaurantResponse> retrieveFavouriteRestaurants(String customerId) {
        return null;
    }
}