package com.devshaks.delivery.payments;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service", url = "${application.config.payment-service-url}")
public interface PaymentFeignClient {
    @PostMapping
    void requestPayment(@RequestBody @Valid PaymentRequest paymentRequest);
}
