package com.devshaks.delivery.customer.customer;

import com.devshaks.delivery.customer.address.Address;

public record CustomerResponse(
        Integer id,
        String username,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Address address) {
}
