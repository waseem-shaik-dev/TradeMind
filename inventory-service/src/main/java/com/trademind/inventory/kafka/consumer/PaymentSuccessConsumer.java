package com.trademind.inventory.kafka.consumer;

import com.trademind.events.payment.PaymentSuccessEvent;
import com.trademind.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.kafka.support.Acknowledgment;

@Service
@RequiredArgsConstructor
public class PaymentSuccessConsumer {

    private final InventoryService inventoryService;

    @KafkaListener(
            topics = "payment.success",
            groupId = "inventory-service"
    )
    @Transactional
    public void onPaymentSuccess(PaymentSuccessEvent event,Acknowledgment acknowledgment) {

        inventoryService.finalizeReservedStock(
                event.checkoutId()
        );
        acknowledgment.acknowledge();
    }
}
