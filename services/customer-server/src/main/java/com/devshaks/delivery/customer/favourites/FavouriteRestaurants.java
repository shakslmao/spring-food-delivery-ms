package com.devshaks.delivery.customer.favourites;

import com.devshaks.delivery.customer.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavouriteRestaurants {

    @Id
    private Integer id;

    @ManyToMany(mappedBy = "favouriteRestaurants")
    private List<Customer> customers;

    private String name;
    private LocalDateTime createdAt;

    public FavouriteRestaurants(Integer id, String name, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }
}