package com.devshaks.delivery.payments;

public record StripePaymentResponse(
        String clientSecret, String paymentIntentId) {
}
