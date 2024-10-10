package com.devshaks.delivery.customer;

import com.devshaks.delivery.customer.address.Address;

public record CustomerResponse(
        String id,
        String username,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Address address
) {
}
