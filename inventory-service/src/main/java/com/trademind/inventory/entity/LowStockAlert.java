package com.trademind.inventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "low_stock_alerts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LowStockAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long stockItemId;
    private LocalDateTime triggeredAt;
    private boolean resolved;
}
