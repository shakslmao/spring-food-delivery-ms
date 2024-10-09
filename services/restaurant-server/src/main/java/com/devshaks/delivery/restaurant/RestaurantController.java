package com.devshaks.delivery.restaurant;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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

    @PostMapping("/{restaurantId}/purchase")
    public ResponseEntity<List<RestaurantPurchaseResponse>> purchaseDelivery(@PathVariable("restaurantId") Integer restaurantId, @RequestBody @Valid List<RestaurantPurchaseRequest> purchaseRequests) {
        List<RestaurantPurchaseResponse> responses = restaurantService.purchaseDelivery(purchaseRequests, restaurantId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    


}
