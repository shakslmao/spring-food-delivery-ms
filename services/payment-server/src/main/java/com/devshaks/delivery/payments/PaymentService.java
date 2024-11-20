package com.devshaks.delivery.payments;

import com.devshaks.delivery.kafka.KafkaPaymentProducer;
import com.devshaks.delivery.kafka.PaymentEvent;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final KafkaPaymentProducer kafkaPaymentProducer;

    // include stripe
    public Integer createPayment(@Valid PaymentRequest paymentRequest) {
        var payments = paymentRepository.save(paymentMapper.mapToPayments(paymentRequest));

        // Kafka Event
        try {
            kafkaPaymentProducer.sendPaymentEvent(new PaymentEvent(
                    payments.getId(),
                    payments.getAmount(),
                    payments.getOrderReference(),
                    payments.getPaymentMethod(),
                    OrderStatus.CONFIRMED));
        } catch (Exception e) {
            log.error("Error sending payment event to order service: {}", e.getMessage());
            throw new RuntimeException("Error sending payment event to order service");
        }

        return payments.getId();
    }

    public StripePaymentResponse createStripePayment(@Valid PaymentRequest paymentRequest) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("amount", paymentRequest.orderAmount().multiply(BigDecimal.valueOf(100)).intValue());
            params.put("currency", "gbp");
            params.put("payment_method_types", List.of("card"));
            params.put("description", "Payment for Order " + paymentRequest.orderReference());

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // save payment locally
            var payment = paymentMapper.mapToStripe(paymentRequest, paymentIntent.getId());
            payment.setStripePaymentId(paymentIntent.getId());
            paymentRepository.save(payment);

            return new StripePaymentResponse(paymentIntent.getClientSecret(), paymentIntent.getId());
        } catch (StripeException e) {
            log.error("Stripe payment failed: {}", e.getMessage());
            throw new RuntimeException("Stripe payment failed: " + e.getMessage(), e);

        }
    }

}
