package com.devshaks.delivery.customer;

import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.devshaks.delivery.config.FeignConfig;

@FeignClient(name = "customer-service", url = "${application.config.customer-service-url}", configuration = FeignConfig.class)
public interface CustomerFeignClient {
    @GetMapping("/customers/{customerId}")
    Optional<CustomerResponse> findCustomerById(@PathVariable("customerId") Integer customerId);
}
