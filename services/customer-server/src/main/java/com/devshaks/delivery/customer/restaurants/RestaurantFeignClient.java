package com.devshaks.delivery.customer.restaurants;

import jakarta.ws.rs.Path;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "restaurant-service", url = "${application.config.restaurant-service-url}")
public interface RestaurantFeignClient {
    @GetMapping("/{restaurantId}")
    RestaurantDTO getRestaurantById(@PathVariable("restaurantId") Integer restaurantId);
}