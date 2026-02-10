package com.trademind.checkout.repository;

import com.trademind.checkout.entity.CheckoutAddressSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheckoutAddressSnapshotRepository
        extends JpaRepository<CheckoutAddressSnapshot, Long> {

    Optional<CheckoutAddressSnapshot> findByCheckoutSessionId(Long checkoutSessionId);
}
