package com.trademind.payment.entity;

import com.trademind.payment.enums.PaymentMethod;
import com.trademind.payment.enums.PaymentProvider;
import com.trademind.payment.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "payment_transactions",
        indexes = {
                @Index(name = "idx_payment_checkout", columnList = "checkoutId"),
                @Index(name = "idx_payment_status", columnList = "status"),
                @Index(name = "idx_payment_provider_ref", columnList = "providerPaymentIntentId")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ---- Ownership ----
    @Column(nullable = false)
    private Long checkoutId;

    @Column(nullable = false)
    private Long userId;

    // ---- Money ----
    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency;

    // ---- Payment details ----
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentProvider paymentProvider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    // ---- Stripe specific (gateway-agnostic fields) ----
    private String providerPaymentIntentId;   // Stripe payment_intent id
    private String providerClientSecret;       // Stripe client_secret
    private String providerChargeId;            // Stripe charge id

    // ---- Failure handling ----
    private String failureReason;
}
