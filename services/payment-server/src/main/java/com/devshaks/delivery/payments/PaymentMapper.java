package com.devshaks.delivery.payments;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class PaymentMapper {
    public Payments mapToPayments(@Valid PaymentRequest paymentRequest) {
        return Payments.builder()
                .customerId(paymentRequest.customer().id())
                .orderReference(paymentRequest.orderReference())
                .paymentMethod(paymentRequest.paymentMethod())
                .amount(paymentRequest.orderAmount())
                .build();
    }

    // stripe payments
    public Payments mapToStripe(@Valid PaymentRequest paymentRequest, String stripePaymentId) {
        return Payments.builder()
                .customerId(paymentRequest.customer().id())
                .orderReference(paymentRequest.orderReference())
                .paymentMethod(paymentRequest.paymentMethod())
                .amount(paymentRequest.orderAmount())
                .stripePaymentId(stripePaymentId)
                .build();
    }
}
