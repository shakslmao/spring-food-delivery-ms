package com.devshaks.delivery.customer;

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
