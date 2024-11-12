package com.devshaks.delivery.customer.favourites;

import com.devshaks.delivery.customer.restaurants.RestaurantDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class FavouriteMapper {
    public FavouriteRestaurants mapFavouritesToRestaurantResponse(RestaurantDTO restaurantDTO) {
        return FavouriteRestaurants.builder()
                .restaurantId(restaurantDTO.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public RestaurantDTO mapRestaurantResponseToFavourites(FavouriteRestaurants favouriteRestaurants) {
        return RestaurantDTO.builder()
                .id(favouriteRestaurants.getRestaurantId())
                .build();
    }

}
