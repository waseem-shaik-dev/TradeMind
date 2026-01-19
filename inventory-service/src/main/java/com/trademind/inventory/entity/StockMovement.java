package com.trademind.inventory.entity;

import com.trademind.inventory.enums.MovementType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_movements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long stockItemId;

    @Enumerated(EnumType.STRING)
    private MovementType type; // IN / OUT / ADJUSTMENT

    private Integer quantity;
    private String referenceId; // billId / orderId

    private LocalDateTime timestamp;
}
