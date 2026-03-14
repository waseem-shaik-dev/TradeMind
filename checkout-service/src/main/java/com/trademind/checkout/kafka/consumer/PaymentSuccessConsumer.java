package com.trademind.checkout.kafka.consumer;

import com.trademind.checkout.repository.CheckoutSessionRepository;
import com.trademind.checkout.enums.CheckoutStatus;
import com.trademind.events.payment.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentSuccessConsumer {

    private final CheckoutSessionRepository checkoutRepo;

    @KafkaListener(
            topics = "payment.success",
            groupId = "checkout-service"
    )
    @Transactional
    public void onPaymentSuccess(PaymentSuccessEvent event, Acknowledgment acknowledgment) {

        checkoutRepo.findById(event.checkoutId())
                .ifPresent(session -> {

                    if (session.getStatus() == CheckoutStatus.CONFIRMED) {
                        return; // idempotent
                    }

                    session.setStatus(CheckoutStatus.CONFIRMED);

                });
        acknowledgment.acknowledge();
    }
}
