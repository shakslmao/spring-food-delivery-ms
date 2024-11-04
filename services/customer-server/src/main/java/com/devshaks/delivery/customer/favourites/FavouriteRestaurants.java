package com.devshaks.delivery.customer.favourites;

import com.devshaks.delivery.customer.customer.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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

    private String restaurantName;
    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "favouriteRestaurants")
    private List<Customer> customers;

}
