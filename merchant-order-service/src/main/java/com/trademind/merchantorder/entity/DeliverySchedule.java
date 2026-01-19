package com.trademind.merchantorder.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "delivery_schedules")
@Getter
@Setter @NoArgsConstructor
@AllArgsConstructor
public class DeliverySchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private LocalDate expectedDeliveryDate;
    private LocalDate actualDeliveryDate;
}
