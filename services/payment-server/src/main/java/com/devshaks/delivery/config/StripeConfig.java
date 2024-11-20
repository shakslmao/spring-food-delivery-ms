package com.devshaks.delivery.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {
    @Value("${stripe.api.secret-api}")
    private String stripeApiKey;

    @PostConstruct
    public void initSecretKey() {
        Stripe.apiKey = stripeApiKey;
    }
}
