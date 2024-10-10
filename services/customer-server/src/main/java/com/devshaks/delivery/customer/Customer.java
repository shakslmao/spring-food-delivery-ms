package com.devshaks.delivery.customer;

import com.devshaks.delivery.customer.address.Address;
import com.devshaks.delivery.customer.favourites.FavouriteRestaurants;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document
public class Customer {
    @Id
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Address address;

    private List<FavouriteRestaurants> favouriteRestaurants;
}
