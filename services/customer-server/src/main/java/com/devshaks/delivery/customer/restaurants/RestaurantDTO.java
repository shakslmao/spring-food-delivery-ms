package com.devshaks.delivery.customer.restaurants;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RestaurantDTO {
    private Integer id;
    private String name;
    private String address;
    private String contactNumber;
    private Double rating;

}