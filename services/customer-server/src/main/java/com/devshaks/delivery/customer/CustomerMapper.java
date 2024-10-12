package com.devshaks.delivery.customer;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {
    public Customer mapCustomerToRequest(@Valid CustomerRequest customerRequest) {
        if (customerRequest == null) {
            return null;
        }
        return Customer.builder()
                .id(customerRequest.id())
                .username(customerRequest.username())
                .firstName(customerRequest.firstName())
                .lastName(customerRequest.lastName())
                .email(customerRequest.email())
                .phoneNumber(customerRequest.phoneNumber())
                .address(customerRequest.address())
                .build();
    }

    public CustomerResponse mapCustomerToResponse(Customer customer) {
        if (customer == null)
            return null;

        return new CustomerResponse(
                customer.getId(),
                customer.getUsername(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getAddress());
    }
}
