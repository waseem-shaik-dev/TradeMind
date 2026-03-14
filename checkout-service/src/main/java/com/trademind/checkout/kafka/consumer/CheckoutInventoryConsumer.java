package com.trademind.checkout.kafka.consumer;

import com.trademind.checkout.service.CheckoutService;
import com.trademind.events.inventory.InventoryReserveFailedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckoutInventoryConsumer {

    private final CheckoutService checkoutService;

    @KafkaListener(
            topics = "inventory.reserve.failed",
            groupId = "checkout-service"
    )
    public void handleInventoryReserveFailed(
            InventoryReserveFailedEvent event,
            Acknowledgment acknowledgment
    ) {
        checkoutService.updateCheckoutAfterPayment(
                event.checkoutId(),
                "CANCELLED"
        );
        acknowledgment.acknowledge();
    }
}
