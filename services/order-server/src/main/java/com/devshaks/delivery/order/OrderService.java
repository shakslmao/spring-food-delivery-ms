package com.devshaks.delivery.order;

import com.devshaks.delivery.customer.CustomerResponse;
import com.devshaks.delivery.kafka.KafkaOrderProducer;
import com.devshaks.delivery.kafka.OrderConfirmation;
import com.devshaks.delivery.orderline.OrderLines;
import com.devshaks.delivery.payments.PaymentFeignClient;
import com.devshaks.delivery.payments.PaymentRequest;
import com.devshaks.delivery.restaurant.RestaurantFeignClient;
import com.devshaks.delivery.restaurant.RestaurantPurchaseRequest;
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
    public Integer createOrder(@Valid OrderRequest orderRequest) {
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
            Integer restaurantId = orderRequest.restaurantProducts().get(0).restaurantId();
            log.info("Attempting to purchase delivery from restaurant ID: {}", restaurantId);
            log.debug("Purchase request details: {}", orderRequest.restaurantProducts());

            // Create a list of RestaurantPurchaseRequest objects
            List<RestaurantPurchaseRequest> restaurantPurchaseRequests = orderRequest.restaurantProducts().stream()
                    .map(product -> new RestaurantPurchaseRequest(product.restaurantId(), product.items()))
                    .collect(Collectors.toList());

            var purchasedProducts = this.restaurantFeignClient.purchaseDelivery(restaurantId, restaurantPurchaseRequests);
            log.info("Successfully purchased products from restaurant");

            // Map order request to Order entity and set status to PENDING
            var order = orderMapper.mapToOrder(orderRequest);
            order.setOrderStatus(OrderStatus.PENDING);

            // Create OrderLines and calculate total amount
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
                paymentFeignClient.requestPayment(paymentRequest);
            } catch (Exception paymentError) {
                log.error("Error processing payment: ", paymentError);
                throw new BusinessException("Error Processing Payment: " + paymentError.getMessage());
            }

            // Send order confirmation event to Kafka
            try {
                kafkaOrderProducer.sendOrderConfirmation(new OrderConfirmation(
                        totalOrderAmount,
                        orderRequest.paymentMethod(),
                        customer,
                        purchasedProducts));
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
