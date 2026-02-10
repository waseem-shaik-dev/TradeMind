package com.trademind.checkout.repository;

import com.trademind.checkout.entity.CheckoutPaymentSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheckoutPaymentSnapshotRepository
        extends JpaRepository<CheckoutPaymentSnapshot, Long> {

    Optional<CheckoutPaymentSnapshot> findByCheckoutSessionId(Long checkoutSessionId);

    Optional<CheckoutPaymentSnapshot> findByPaymentIntentId(String paymentIntentId);
}
