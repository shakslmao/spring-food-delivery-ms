package com.devshaks.delivery.customer.address;

public record AddressRequest(
        Integer id,
        String street,
        String houseNumber,
        String city,
        String postCode,
        String country
) {
}
