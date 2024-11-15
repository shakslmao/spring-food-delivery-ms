package com.devshaks.delivery.payments;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payments, Integer> {
}
