package com.devshaks.delivery.customer.favourites;

import com.devshaks.delivery.customer.restaurants.RestaurantResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class FavouriteMapper {
    public FavouriteRestaurants mapFavouritesToRestaurantResponse(@Valid RestaurantResponse restaurantResponse) {
        return FavouriteRestaurants.builder()
                .restaurantName(restaurantResponse.name())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
