package com.devshaks.delivery.payments;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service", url = "${application.config.payment-service-url}")
public interface PaymentFeignClient {
    @PostMapping
    Integer requestPayment(@RequestBody PaymentRequest paymentRequest);
}
