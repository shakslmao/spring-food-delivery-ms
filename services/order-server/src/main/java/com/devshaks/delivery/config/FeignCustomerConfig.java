package com.devshaks.delivery.config;

import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignCustomerConfig {
    @Bean
    public Request.Options options() {
        return new Request.Options(5000, 5000);
    }
}
