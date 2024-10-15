package com.devshaks.delivery.customer;

import com.devshaks.delivery.customer.address.Address;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {
    public Customer mapCustomerToRequest(@Valid CustomerRequest customerRequest) {
        if (customerRequest == null) {
            return null;
        }
        Address address = mapAddressToRequest(customerRequest.address());
        return Customer.builder()
                .id(customerRequest.id())
                .username(customerRequest.username())
                .firstName(customerRequest.firstName())
                .lastName(customerRequest.lastName())
                .email(customerRequest.email())
                .phoneNumber(customerRequest.phoneNumber())
                .address(address)
                .build();
    }

    private Address mapAddressToRequest(Address addressRequest) {
        if (addressRequest == null) {
            return null;
        }
        return Address.builder()
                .street(addressRequest.getStreet())
                .houseNumber(addressRequest.getHouseNumber())
                .city(addressRequest.getCity())
                .postCode(addressRequest.getPostCode())
                .country(addressRequest.getCountry())
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
