package com.devshaks.delivery.restaurant;

import com.devshaks.delivery.cuisine.CuisineTypes;
import com.devshaks.delivery.cuisine.CuisineTypesRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantMapper restaurantMapper;
    private final RestaurantRepository restaurantRepository;
    private final CuisineTypesRepository cuisineTypesRepository;

    public Integer createRestaurant(@Valid RestaurantRequest restaurantRequest) {
        var restaurant = restaurantMapper.mapRestaurantToRequest(restaurantRequest);
        return restaurantRepository.save(restaurant).getId();
    }

    public List<RestaurantPurchaseResponse> purchaseDelivery(List<RestaurantPurchaseRequest> restaurantPurchaseRequests, Integer restaurantId) {
        // Validate that all requests are for the same restaurant
        if (restaurantPurchaseRequests.stream().anyMatch(request -> !request.restaurantId().equals(restaurantId))) {
            throw new IllegalArgumentException("All purchase requests must be for the same restaurant");
        }

        // Fetch the restaurant
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        // fetch all requested cuisines at once
        List<Integer> cuisineIds = restaurantPurchaseRequests.stream()
                .map(RestaurantPurchaseRequest::cuisineId)
                .toList();

        List<CuisineTypes> cuisines = cuisineTypesRepository.findAllById(cuisineIds);

        // validate that all requested cuisines were found
        if (cuisines.size() != cuisineIds.size()) {
            throw new EntityNotFoundException("One or more cuisine types not found");
        }

        // validate that all requested cuisines belong to the restaurant
        cuisines.forEach(cuisine -> {
            if (!cuisine.getRestaurant().getId().equals(restaurantId)) {
                throw new IllegalArgumentException("Cuisine type does not belong to the restaurant");
            }
        });

        return cuisines.stream()
                .map(cuisineTypes -> restaurantMapper.toRestaurantPurchaseResponse(restaurant, cuisineTypes))
                .collect(Collectors.toList());
    }
}
