package com.devshaks.delivery.orderline;

import com.devshaks.delivery.order.Order;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OrderLines {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer cuisineId;
    private String cuisineName;
    private int quantity;
    private BigDecimal price;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
}
