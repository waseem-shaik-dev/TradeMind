package com.trademind.merchantorder.entity;

import com.trademind.merchantorder.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_approvals")
@Getter
@Setter@NoArgsConstructor
@AllArgsConstructor
public class OrderApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private Long approvedBy;
    private LocalDateTime approvedAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
