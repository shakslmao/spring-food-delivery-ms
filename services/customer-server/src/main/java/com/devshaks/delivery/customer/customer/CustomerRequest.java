package com.devshaks.delivery.customer.customer;

import com.devshaks.delivery.customer.address.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(
        Integer id,
        @NotNull(message = "Username is required") String username,
        @NotNull(message = "First name is required") String firstName,
        @NotNull(message = "Last name is required") String lastName,
        @Email(message = "Email is required") String email,
        @NotNull(message = "Phone number is required") String phoneNumber,
        Address address) {
}
