package com.devshaks.delivery.customer.favourites;


import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class FavouriteRestaurants {
    private Integer restaurantId;
    private String name;
    private LocalDateTime createdAt;

}
