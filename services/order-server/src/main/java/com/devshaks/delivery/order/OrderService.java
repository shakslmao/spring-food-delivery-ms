package com.devshaks.delivery.order;

import com.devshaks.delivery.customer.CustomerResponse;
import com.devshaks.delivery.kafka.KafkaOrderProducer;
import com.devshaks.delivery.kafka.OrderConfirmation;
import com.devshaks.delivery.orderline.OrderLines;
import com.devshaks.delivery.payments.PaymentFeignClient;
import com.devshaks.delivery.payments.PaymentRequest;
import com.devshaks.delivery.restaurant.RestaurantFeignClient;
import com.devshaks.delivery.restaurant.RestaurantPurchaseRequest;
import com.devshaks.delivery.restaurant.RestaurantPurchaseResponse;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.devshaks.delivery.customer.CustomerFeignClient;
import com.devshaks.delivery.exceptions.BusinessException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final CustomerFeignClient customerFeignClient;
    private final RestaurantFeignClient restaurantFeignClient;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final PaymentFeignClient paymentFeignClient;
    private final KafkaOrderProducer kafkaOrderProducer;

    @Transactional
    public Integer createOrderPurchase(@Valid OrderRequest orderRequest) {
        try {
            if (orderRequest.customerId() == null) {
                throw new BusinessException("Customer ID is required.");
            }

            log.info("Fetching customer with ID: {}", orderRequest.customerId());
            var customer = this.customerFeignClient.findCustomerById(orderRequest.customerId())
                    .orElseThrow(() -> new BusinessException("Customer Was Not Found with ID: " + orderRequest.customerId()));
            log.info("Processing order for customer: {}", customer);

            if (orderRequest.restaurantProducts() == null || orderRequest.restaurantProducts().isEmpty()) {
                throw new BusinessException("No restaurant products provided for the order.");
            }

            // Fetch restaurant ID from the first item in the list
            var restaurantProduct = orderRequest.restaurantProducts().get(0);
            Integer restaurantId = restaurantProduct.restaurantId();
            log.info("Attempting to purchase delivery from restaurant ID: {}", restaurantId);
            log.debug("Purchase request details: {}", orderRequest.restaurantProducts());

            boolean isSingleRestaurant = orderRequest.restaurantProducts()
                    .stream()
                    .allMatch(product -> product.restaurantId().equals(restaurantId));

            if (!isSingleRestaurant) {
                throw new BusinessException("Order contains products from multiple restaurants.");
            }

            // send a single restaraunt purchase request to restaruant microservice
            RestaurantPurchaseRequest purchaseRequest = new RestaurantPurchaseRequest(
                    restaurantId,
                    restaurantProduct.items()
            );

            RestaurantPurchaseResponse purchasedProducts = this.restaurantFeignClient.purchaseDelivery(restaurantId, purchaseRequest);
            log.info("Successfully purchased products from restaurant");

            // Map order request to Order entity and set status to PENDING
            var order = orderMapper.mapToOrder(orderRequest);
            order.setOrderStatus(OrderStatus.PENDING);

            // Create OrderLines and calculate total amount
            List<OrderLines> items = purchasedProducts.items()
                    .stream()
                    .map(item -> {
                        OrderLines orderLines = new OrderLines();
                        orderLines.setCuisineId(item.cuisineId());
                        orderLines.setCuisineName(item.name());
                        orderLines.setQuantity(item.quantity());
                        orderLines.setPrice(item.price());
                        orderLines.setTotalPrice(item.price().multiply(BigDecimal.valueOf(item.quantity())));
                        orderLines.setOrder(order);
                        return orderLines;
                    })
                    .collect(Collectors.toList());

            order.setOrderLines(items);

            BigDecimal totalOrderAmount = items.stream()
                    .map(OrderLines::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            order.setOrderAmount(totalOrderAmount);

            if (totalOrderAmount == null || totalOrderAmount.compareTo(BigDecimal.ZERO) <= 0) {
                log.error("Order amount is invalid: {}", totalOrderAmount);
                throw new BusinessException("Order amount is invalid: " + totalOrderAmount);
            }

            // Save Order
            var savedOrder = orderRepository.save(order);

            // Prepare and send payment request
            var paymentRequest = new PaymentRequest(
                    totalOrderAmount,
                    orderRequest.paymentMethod(),
                    order.getOrderReference(),
                    savedOrder.getId(),
                    customer);

            try {
                log.info("Sending Payment Request: {}", paymentRequest);
                paymentFeignClient.requestPayment(paymentRequest);
                log.info("Payment request processed successfully");
            } catch (FeignException e) {
                log.error("Error processing payment: Status: {}, Message: {}", e.status(), e.getMessage());
                throw new BusinessException("Error Processing Payment: " + e.getMessage());
            }

            // Send order confirmation event to Kafka
            try {
                kafkaOrderProducer.sendOrderConfirmation(new OrderConfirmation(
                        totalOrderAmount,
                        orderRequest.paymentMethod(),
                        customer,
                       List.of(purchasedProducts)));
            } catch (Exception kafkaError) {
                log.error("Error sending order confirmation: ", kafkaError);
                throw new BusinessException("Error Sending Order Confirmation: " + kafkaError.getMessage());
            }

            return order.getId();

        } catch (Exception error) {
            log.error("Error creating order: ", error);
            throw new BusinessException("Error Creating Order: " + error.getMessage());
        }
    }

}
