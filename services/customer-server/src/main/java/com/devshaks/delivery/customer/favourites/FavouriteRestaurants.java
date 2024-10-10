package com.devshaks.delivery.customer.favourites;


import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Validated
public class FavouriteRestaurants {

    private Integer restaurantId;
    private LocalDateTime addedAt;
}
