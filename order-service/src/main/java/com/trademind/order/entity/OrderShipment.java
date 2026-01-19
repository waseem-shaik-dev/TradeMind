package com.trademind.order.entity;

import com.trademind.order.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "order_shipments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderShipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private Long addressId;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus shipmentStatus;
}
