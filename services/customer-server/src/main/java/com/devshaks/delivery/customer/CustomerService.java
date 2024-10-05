package com.devshaks.delivery.customer;

import com.devshaks.delivery.customer.exceptions.CustomerNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public String createCustomer(@Valid CustomerRequest customerRequest) {
        var customer = customerRepository.save(customerMapper.toCustomer(customerRequest));
        return customer.getId();
    }

    public void updateCustomer(@Valid CustomerRequest customerRequest) {
        var customer =  customerRepository.findById(customerRequest.id()).orElseThrow(() -> new CustomerNotFoundException("Customer With Id: " + customerRequest.id() + " Not Found"));
        updateCustomerCredentials(customer, customerRequest);
        customerRepository.save(customer);
    }

    private  void updateCustomerCredentials(Customer customer, CustomerRequest customerRequest) {
        if (StringUtils.isNotBlank(customerRequest.email())) {
            customer.setFirstName(customerRequest.firstName());
        }

        if (StringUtils.isNotBlank(customerRequest.lastName())) {
            customer.setLastName(customerRequest.lastName());
        }

        if (StringUtils.isNotBlank(customerRequest.email())) {
            customer.setEmail(customerRequest.email());
        }

        if (StringUtils.isNotBlank(customerRequest.phoneNumber())) {
            customer.setPhoneNumber(customerRequest.phoneNumber());
        }

        if (customerRequest.address() != null) {
            customer.setAddress(customerRequest.address());
        }



    }


}