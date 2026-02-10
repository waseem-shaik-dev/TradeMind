package com.trademind.checkout.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "checkout_address_snapshots")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutAddressSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ---- Relation ----
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checkout_session_id", nullable = false)
    private CheckoutSession checkoutSession;

    // ---- Snapshot ----
    private String fullName;
    private String phone;

    private String addressLine1;
    private String addressLine2;

    private String city;
    private String state;
    private String postalCode;
    private String country;
}
