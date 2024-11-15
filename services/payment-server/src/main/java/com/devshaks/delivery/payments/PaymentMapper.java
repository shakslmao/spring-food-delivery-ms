package com.devshaks.delivery.payments;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class PaymentMapper {
    public Payments mapToPayments(@Valid PaymentRequest paymentRequest) {
        return Payments.builder()
                .id(paymentRequest.id())
                .orderReference(paymentRequest.orderReference())
                .paymentMethod(paymentRequest.paymentMethod())
                .amount(paymentRequest.paymentAmount())
                .build();

    }
}
