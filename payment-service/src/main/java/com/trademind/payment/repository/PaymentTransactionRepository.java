package com.trademind.payment.repository;

import com.trademind.payment.entity.PaymentTransaction;
import com.trademind.payment.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentTransactionRepository
        extends JpaRepository<PaymentTransaction, Long> {

    // 🔹 One checkout → one payment (important invariant)
    Optional<PaymentTransaction> findByCheckoutId(Long checkoutId);

    // 🔹 Stripe webhook / verification lookup
    Optional<PaymentTransaction> findByProviderPaymentIntentId(
            String providerPaymentIntentId
    );

    // 🔹 Retry / monitoring / reconciliation
    List<PaymentTransaction> findByStatus(PaymentStatus status);

    // 🔹 Safety: prevent duplicate payment creation
    boolean existsByCheckoutId(Long checkoutId);
}
