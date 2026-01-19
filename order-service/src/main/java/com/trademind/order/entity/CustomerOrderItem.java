package com.trademind.order.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_order_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CustomerOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private Long productId;

    private Integer quantity;
    private BigDecimal price;
    private BigDecimal lineTotal;
}
