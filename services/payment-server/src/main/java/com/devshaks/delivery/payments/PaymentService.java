package com.devshaks.delivery.payments;

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

    public Integer createPayment(@Valid PaymentRequest paymentRequest) {
        var payments = paymentRepository.save(paymentMapper.mapToPayments(paymentRequest));
        // TODO: Send payment to payment gateway
        log.info("Payment created: {}", payments);
        return payments.getId();
    }
}
