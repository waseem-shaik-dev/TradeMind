package com.trademind.checkout.entity;

import com.trademind.checkout.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "checkout_sessions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutSession extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ---- Ownership ----
    @Column(nullable = false)
    private Long userId;


    private String userEmail;

    @Column(nullable = false)
    private Long cartId;

    @Column(nullable = false)
    private Long sourceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SourceType sourceType;

    // ---- Status ----
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CheckoutStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_type")
    private DeliveryType deliveryType;

    @Enumerated(EnumType.STRING)
    @Column(name = "buyer_type", nullable = false)
    private BuyerType buyerType;

    // ---- Price Summary ----
    @Column(nullable = false)
    private BigDecimal subtotalAmount;

    @Column(nullable = false)
    private BigDecimal taxAmount;

    @Column(nullable = false)
    private BigDecimal discountAmount;

    @Column(nullable = false)
    private BigDecimal deliveryFee;

    @Column(nullable = false)
    private BigDecimal grandTotal;

    @Column(nullable = false)
    private String currency;

    // ---- Expiry ----
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    // ---- Relationships ----
    @OneToMany(
            mappedBy = "checkoutSession",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<CheckoutItem> items = new ArrayList<>();

    @OneToOne(
            mappedBy = "checkoutSession",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private CheckoutAddressSnapshot addressSnapshot;

    @OneToOne(
            mappedBy = "checkoutSession",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private CheckoutPaymentSnapshot paymentSnapshot;

    // ---- Helper methods ----
    public void addItem(CheckoutItem item) {
        items.add(item);
        item.setCheckoutSession(this);
    }

    public void setAddressSnapshot(CheckoutAddressSnapshot address) {
        this.addressSnapshot = address;
        address.setCheckoutSession(this);
    }

    public void setPaymentSnapshot(CheckoutPaymentSnapshot payment) {
        this.paymentSnapshot = payment;
        payment.setCheckoutSession(this);
    }
}
