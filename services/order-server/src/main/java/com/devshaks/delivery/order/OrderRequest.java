package com.devshaks.delivery.order;

import java.util.List;
import com.devshaks.delivery.restaurant.RestaurantPurchaseRequest;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
        Integer id,
        @NotNull(message = "Payment method is required") PaymentMethod paymentMethod,

        @NotNull(message = "Customer is Required") Integer customerId,

        @NotNull(message = "Order Items are Required") List<RestaurantPurchaseRequest> restaurantProducts) {

}
