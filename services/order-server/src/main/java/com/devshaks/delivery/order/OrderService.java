package com.devshaks.delivery.order;

import com.devshaks.delivery.restaurant.RestaurantFeignClient;
import org.springframework.stereotype.Service;

import com.devshaks.delivery.customer.CustomerFeignClient;
import com.devshaks.delivery.exceptions.BusinessException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CustomerFeignClient customerFeignClient;
    private final RestaurantFeignClient restaurantFeignClient;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public Integer createOrder(@Valid OrderRequest orderRequest) {
        var customer = this.customerFeignClient.findCustomerById(orderRequest.customerId())
                .orElseThrow(() -> new BusinessException("Customer Was Not Found with ID: " + orderRequest.customerId()));
        var purchasedProducts = this.restaurantFeignClient.purchaseDelivery(orderRequest.restaurantProducts());
        var order = this.orderRepository.save(orderMapper.mapToOrder(orderRequest));


        return 1;
    }

}
