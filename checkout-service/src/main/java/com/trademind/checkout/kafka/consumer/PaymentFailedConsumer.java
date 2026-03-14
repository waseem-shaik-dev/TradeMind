package com.trademind.checkout.kafka.consumer;

import com.trademind.checkout.repository.CheckoutSessionRepository;
import com.trademind.checkout.enums.CheckoutStatus;
import com.trademind.events.payment.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PaymentFailedConsumer {

    private final CheckoutSessionRepository checkoutRepo;

    @KafkaListener(
            topics = "payment.failed",
            groupId = "checkout-service"
    )
    @Transactional
    public void onPaymentFailed(PaymentFailedEvent event, Acknowledgment acknowledgment) {

        checkoutRepo.findById(event.checkoutId())
                .ifPresent(session -> {

                    if (session.getStatus() == CheckoutStatus.CANCELLED) {
                        return;
                    }

                    session.setStatus(CheckoutStatus.CANCELLED);
                });
        acknowledgment.acknowledge();
    }
}
