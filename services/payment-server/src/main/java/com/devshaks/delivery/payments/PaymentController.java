package com.devshaks.delivery.payments;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Integer> createPayment(@RequestBody @Valid PaymentRequest paymentRequest) {
        log.info("Creating payment: {}", paymentRequest);
        Integer request = paymentService.createPayment(paymentRequest);
        log.info("Payment created with ID: {}", request);
        return ResponseEntity.ok().build();
    }
}

