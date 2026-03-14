package com.trademind.inventory.kafka.consumer;

import com.trademind.events.payment.PaymentFailedEvent;
import com.trademind.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentFailedConsumer {

    private final InventoryService inventoryService;

    @KafkaListener(
            topics = "payment.failed",
            groupId = "inventory-service"
    )
    @Transactional
    public void onPaymentFailed(PaymentFailedEvent event, Acknowledgment acknowledgment) {

        inventoryService.releaseReservedStock(
                event.checkoutId()
        );
        acknowledgment.acknowledge();
    }
}
