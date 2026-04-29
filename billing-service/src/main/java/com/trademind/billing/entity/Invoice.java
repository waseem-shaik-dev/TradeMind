package com.trademind.billing.entity;

import com.trademind.billing.enums.PaymentMethod;
import com.trademind.billing.enums.PaymentStatus;
import com.trademind.billing.enums.SourceType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "invoices",
        indexes = {
                @Index(name = "idx_invoice_order", columnList = "order_id"),
                @Index(name = "idx_invoice_user", columnList = "user_id"),
                @Index(name = "idx_invoice_source", columnList = "source_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_invoice_order", columnNames = "order_id")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String invoiceNumber;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private String orderNumber;

    @Column(name = "user_id", nullable = false)
    private Long userId;



    @Column(name = "source_id", nullable = false)
    private Long sourceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false)
    private SourceType sourceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    // Pricing snapshot
    @Column(nullable = false)
    private BigDecimal subtotal;

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

    // Address snapshot (stored as JSON string)
    @Column(columnDefinition = "TEXT", nullable = false)
    private String addressSnapshot;

    @Column(columnDefinition = "TEXT")
    private String sellerSnapshot;

    // Hash for verification
    @Column(nullable = false)
    private String invoiceHash;

    // Audit
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Relations
    @OneToMany(
            mappedBy = "invoice",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<InvoiceLineItem> items = new ArrayList<>();

    // Helper
    public void addItem(InvoiceLineItem item) {
        item.setInvoice(this);
        this.items.add(item);
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}