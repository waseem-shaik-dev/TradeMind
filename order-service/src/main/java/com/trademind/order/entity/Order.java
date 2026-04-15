package com.trademind.order.entity;

import com.trademind.order.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity
@Table(
        name = "orders",
        indexes = {
                @Index(name = "idx_order_user", columnList = "user_id"),
                @Index(name = "idx_order_source", columnList = "source_id"),
                @Index(name = "idx_order_status", columnList = "order_status"),
                @Index(name = "idx_order_created", columnList = "created_at")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_checkout_id", columnNames = "checkout_id")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Human readable order number
    @Column(nullable = false, unique = true)
    private String orderNumber;

    // Checkout reference (idempotency key)
    @Column(name = "checkout_id", nullable = false)
    private Long checkoutId;

    @Column(name = "cart_id")
    private Long cartId;

    // Customer / Retailer
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "user_email")
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "buyer_type", nullable = false)
    private BuyerType buyerType;

    // Merchant / Retailer
    @Column(name = "source_id", nullable = false)
    private Long sourceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false)
    private SourceType sourceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_type", nullable = false)
    private DeliveryType deliveryType;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    // Pricing
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

    // Future billing support
    private Long invoiceId;

    @Version
    private Long version;

    // Audit
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ------------------------------
    // Relations
    // ------------------------------

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<OrderLineItem> lineItems = new ArrayList<>();

    @OneToOne(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private OrderAddressSnapshot addressSnapshot;

    @Column(columnDefinition = "TEXT")
    private String sellerSnapshot;

    // ------------------------------
    // Helper Methods (Aggregate logic)
    // ------------------------------

    public void addLineItem(OrderLineItem item) {
        item.setOrder(this);
        this.lineItems.add(item);
    }

    public void setAddressSnapshot(OrderAddressSnapshot snapshot) {
        snapshot.setOrder(this);
        this.addressSnapshot = snapshot;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
