package com.devshaks.delivery.restaurant;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

@Validated
public record CuisinePurchaseRequest(
        @NotNull(message = "Cuisine must be selected")
        Integer cuisineId,

        @Positive
        int quantity
) {
}
