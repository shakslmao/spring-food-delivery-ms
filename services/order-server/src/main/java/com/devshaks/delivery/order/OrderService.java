package com.devshaks.delivery.order;

import com.devshaks.delivery.orderline.OrderLines;
import com.devshaks.delivery.payments.PaymentFeignClient;
import com.devshaks.delivery.payments.PaymentRequest;
import com.devshaks.delivery.restaurant.RestaurantFeignClient;
import org.springframework.stereotype.Service;

import com.devshaks.delivery.customer.CustomerFeignClient;
import com.devshaks.delivery.exceptions.BusinessException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CustomerFeignClient customerFeignClient;
    private final RestaurantFeignClient restaurantFeignClient;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final PaymentFeignClient paymentFeignClient;

    public Integer createOrder(@Valid OrderRequest orderRequest) {
        try {
            var customer = this.customerFeignClient.findCustomerById(orderRequest.customerId())
                    .orElseThrow(() -> new BusinessException("Customer Was Not Found with ID: " + orderRequest.customerId()));
            var purchasedProducts = this.restaurantFeignClient.purchaseDelivery(orderRequest.restaurantProducts());
            var order = orderMapper.mapToOrder(orderRequest);
            order.setOrderStatus(OrderStatus.PENDING);

            List<OrderLines> items = purchasedProducts.stream()
                    .flatMap(response -> response.items().stream())
                    .map(item -> {
                        OrderLines orderLines = new OrderLines();
                        orderLines.setCuisineId(item.cuisineId());
                        orderLines.setCuisineName(item.name());
                        orderLines.setQuantity(item.quantity());
                        orderLines.setPrice(item.price());
                        orderLines.setTotalPrice(item.price().multiply(BigDecimal.valueOf(item.quantity())));
                        orderLines.setOrder(order);
                        return orderLines;
                    }).collect(Collectors.toList());

            order.setOrderLines(items);

            BigDecimal totalOrderAmount = items.stream()
                    .map(OrderLines::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            order.setOrderAmount(totalOrderAmount);

            var paymentRequest = new PaymentRequest(
                    orderRequest.orderAmount(),
                    orderRequest.paymentMethod(),
                    order.getOrderReference(),
                    order.getId(),
                    customer
            );

            paymentFeignClient.requestPayment(paymentRequest);



            var savedOrder = orderRepository.save(order);
            return savedOrder.getId();
        } catch (Exception error) {
            throw new BusinessException("Error Creating Order: " + error.getMessage());
        }
    }
}
