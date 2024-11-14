package com.devshaks.delivery.customer;

import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service", url = "${application.config.customer-service-url}")
public interface CustomerFeignClient {
    @GetMapping("/{customerId}")
    Optional<CustomerResponse> findCustomerById(@PathVariable("customerId") Integer customerId);
}
