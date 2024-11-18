package com.devshaks.delivery.kafka;

import com.devshaks.delivery.order.Order;
import com.devshaks.delivery.order.OrderRepository;
import com.devshaks.delivery.order.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaOrderConsumer {
    private final OrderRepository orderRepository;

    @KafkaListener(topics = "payment-topic", groupId = "orderGroup")
    public void handlePaymentEvent(PaymentEvent paymentEvent) {
        log.info("Received Payment Event: {}", paymentEvent);
        try {
            Order order = orderRepository.findByOrderReference(paymentEvent.orderReference())
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            if (OrderStatus.CONFIRMED.equals(paymentEvent.orderStatus())) {
                order.setOrderStatus(OrderStatus.CONFIRMED);
                orderRepository.save(order);
                log.info("Order status updated: {}", order);
            } else {
                log.warn("Payment failed for Order Reference: {}", paymentEvent.orderReference());
                order.setOrderStatus(OrderStatus.FAILED);
                orderRepository.save(order);
            }
        } catch (Exception e) {
            log.error("Error processing payment event: {}", e.getMessage());
        }
    }
}
