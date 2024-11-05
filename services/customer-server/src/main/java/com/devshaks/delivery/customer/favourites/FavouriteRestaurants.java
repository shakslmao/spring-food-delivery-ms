package com.devshaks.delivery.customer.favourites;

import com.devshaks.delivery.customer.customer.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class FavouriteRestaurants {

    @Id
    @GeneratedValue
    private Integer id;
    private LocalDateTime createdAt;
    private Integer restaurantId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}