package com.devshaks.delivery.customer.customer;

import com.devshaks.delivery.customer.address.Address;
import com.devshaks.delivery.customer.favourites.FavouriteRestaurants;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Customer {
    @Id
    @GeneratedValue
    private Integer id;

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<FavouriteRestaurants> favouriteRestaurants;
}
