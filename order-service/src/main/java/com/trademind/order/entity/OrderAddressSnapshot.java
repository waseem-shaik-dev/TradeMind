package com.trademind.order.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_address_snapshots")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderAddressSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String phone;

    private String addressLine1;
    private String addressLine2;

    private String city;
    private String state;
    private String postalCode;
    private String country;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;
}
