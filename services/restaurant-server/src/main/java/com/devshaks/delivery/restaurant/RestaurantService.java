package com.devshaks.delivery.restaurant;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantMapper restaurantMapper;
    private final RestaurantRepository restaurantRepository;

    public Integer createRestaurant(@Valid RestaurantRequest restaurantRequest) {
        var restaurant = restaurantMapper.mapRestaurantToRequest(restaurantRequest);
        return restaurantRepository.save(restaurant).getId();
    }

    public List<RestaurantPurchaseResponse> purchaseDelivery(
            List<RestaurantPurchaseRequest> purchaseRequests,
            Integer restaurantId
    ) {
        return null;
    }
}
