package com.trademind.checkout.entity;

import com.trademind.checkout.enums.PaymentMethod;
import com.trademind.checkout.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "checkout_payment_snapshots")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutPaymentSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ---- Relation ----
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checkout_session_id", nullable = false)
    private CheckoutSession checkoutSession;

    // ---- Payment Intent ----
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String paymentProvider;   // RAZORPAY, NONE

    private String paymentIntentId;   // Razorpay order id later

    private BigDecimal amount;
    private String currency;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
