package com.devshaks.delivery.customer;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {
    public Customer toCustomer(@Valid CustomerRequest customerRequest) {
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
}
