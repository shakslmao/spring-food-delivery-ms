package com.devshaks.delivery.restaurant;

import com.devshaks.delivery.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

// use Resilience4j

@FeignClient(name = "restaurant-service", url = "${application.config.restaurant-service-url}", configuration = FeignConfig.class)
public interface RestaurantFeignClient {
    @PostMapping("/{restaurantId}/purchase")
   RestaurantPurchaseResponse purchaseDelivery(
           @PathVariable("restaurantId") Integer restaurantId,
           @RequestBody RestaurantPurchaseRequest restaurantPurchaseRequests);
}

