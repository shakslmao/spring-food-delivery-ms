package com.devshaks.delivery.payments;

import com.devshaks.delivery.kafka.KafkaPaymentProducer;
import com.devshaks.delivery.kafka.PaymentEvent;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        // TODO: Send payment to payment gateway
        log.info("Payment created: {}", payments);
        try {
            kafkaPaymentProducer.sendPaymentEvent(new PaymentEvent(
                    payments.getId(),
                    payments.getAmount(),
                    payments.getOrderReference(),
                    payments.getPaymentMethod(),
                    OrderStatus.CONFIRMED
            ));
        } catch (Exception e) {
            log.error("Error sending payment event to order service: {}", e.getMessage());
            throw new RuntimeException("Error sending payment event to order service");
        }

        return payments.getId();
    }
}
