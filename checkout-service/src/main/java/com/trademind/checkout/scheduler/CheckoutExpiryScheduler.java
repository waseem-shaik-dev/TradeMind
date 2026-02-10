package com.trademind.checkout.scheduler;

import com.trademind.checkout.entity.CheckoutSession;
import com.trademind.checkout.enums.CheckoutStatus;
import com.trademind.checkout.repository.CheckoutSessionRepository;
import com.trademind.checkout.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CheckoutExpiryScheduler {

    private final CheckoutSessionRepository checkoutSessionRepository;
    private final CheckoutService checkoutService;

    /**
     * Runs every 1 minute
     * Cron: second minute hour day month weekday
     */
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void expireCheckouts() {

        List<CheckoutStatus> activeStatuses = List.of(
                CheckoutStatus.CREATED,
                CheckoutStatus.ADDRESS_SELECTED,
                CheckoutStatus.PAYMENT_SELECTED
        );

        List<CheckoutSession> expiredCheckouts =
                checkoutSessionRepository
                        .findByStatusInAndExpiresAtBefore(
                                activeStatuses,
                                LocalDateTime.now()
                        );

        if (expiredCheckouts.isEmpty()) {
            return;
        }

        log.info("Expiring {} checkout(s)", expiredCheckouts.size());

        for (CheckoutSession session : expiredCheckouts) {
            try {
                checkoutService.expireCheckout(session.getId());
            } catch (Exception ex) {
                log.error(
                        "Failed to expire checkout {}",
                        session.getId(),
                        ex
                );
            }
        }
    }
}
