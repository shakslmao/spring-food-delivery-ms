package com.devshaks.delivery.order;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer_order")
public class Order {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true, nullable = false)
    private UUID orderReference;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal orderAmount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private String customerId;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime orderDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedOrderDate;

}