package com.trademind.checkout.repository;

import com.trademind.checkout.entity.CheckoutSession;
import com.trademind.checkout.enums.CheckoutStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CheckoutSessionRepository extends JpaRepository<CheckoutSession, Long> {

    // Fetch active checkout for a user (used for resume checkout)
    Optional<CheckoutSession> findByUserIdAndStatusIn(
            Long userId,
            List<CheckoutStatus> statuses
    );

    // Used by checkout page load
    Optional<CheckoutSession> findByIdAndUserId(Long id, Long userId);

    // Auto-expiry job
    @Query("""
        select cs from CheckoutSession cs
        where cs.status in ('CREATED', 'ADDRESS_SELECTED', 'PAYMENT_SELECTED')
        and cs.expiresAt < :now
    """)
    List<CheckoutSession> findExpiredCheckouts(LocalDateTime now);

    // Safety: prevent double confirmation
    boolean existsByIdAndStatus(Long id, CheckoutStatus status);

    List<CheckoutSession> findByStatusInAndExpiresAtBefore(
            List<CheckoutStatus> statuses,
            LocalDateTime now
    );
}
