package com.devshaks.delivery.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.devshaks.delivery.restaurant.RestaurantPurchaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
        Integer id,
        UUID orderReference,

        @NotNull(message = "Amount must be more than 1")
        BigDecimal orderAmount,

        @NotNull(message = "Payment method is required")
        PaymentMethod paymentMethod,

        @NotNull(message = "Customer is Required")
        @NotEmpty(message = "Customer is Required")
        @NotBlank(message = "Customer is Required")
        Integer customerId,

        @NotNull(message = "Order Items are Required")
        List<RestaurantPurchaseRequest> restaurantProducts
) {

}
