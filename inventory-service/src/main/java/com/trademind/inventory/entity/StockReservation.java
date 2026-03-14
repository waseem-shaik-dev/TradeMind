package com.trademind.inventory.entity;

import com.trademind.inventory.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "stock_reservations",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"checkout_id", "product_id"}
        )
)
@Getter
@Setter
public class StockReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long checkoutId;
    private Long productId;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status; // RESERVED, COMMITTED, RELEASED
}
