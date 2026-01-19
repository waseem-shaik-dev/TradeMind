package com.trademind.order.entity;

import com.trademind.merchantorder.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "order_tracking")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    @Enumerated(EnumType.STRING)
    private OrderStatus currentStatus;

    private LocalDateTime updatedAt;
}

