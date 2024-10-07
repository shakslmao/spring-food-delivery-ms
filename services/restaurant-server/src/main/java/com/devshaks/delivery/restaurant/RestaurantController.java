package com.devshaks.delivery.restaurant;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<Void> createRestaurant(@RequestBody @Valid RestaurantRequest restaurantRequest) {
        Integer restaurantId = restaurantService.createRestaurant(restaurantRequest);
        URI location = URI.create("/api/v1/restaurants/" + restaurantId);
        return ResponseEntity.created(location).build();
    }
}
