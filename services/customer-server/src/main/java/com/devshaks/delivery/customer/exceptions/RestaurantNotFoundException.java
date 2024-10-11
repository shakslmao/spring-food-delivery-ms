package com.devshaks.delivery.customer.exceptions;


import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RestaurantNotFoundException extends RuntimeException {
    private final String exceptionMessage;
}
