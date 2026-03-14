package com.trademind.inventory.kafka.consumer;

import com.trademind.inventory.service.InventoryService;
import com.trademind.events.checkout.InventoryReserveRequestedEvent;
import com.trademind.events.checkout.InventoryReleaseRequestedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryCheckoutEventConsumer {

    private final InventoryService inventoryService;

    // ---------------------------------------------------------
    // Reserve inventory when checkout is confirmed
    // ---------------------------------------------------------

    @KafkaListener(
            topics = "inventory.reserve.requested",
            groupId = "inventory-service"
    )
    public void handleInventoryReserve(
            InventoryReserveRequestedEvent event,
            Acknowledgment acknowledgment
    ) {
        inventoryService.reserveStock(
                event.checkoutId(),
                event.items()
        );
        acknowledgment.acknowledge();
    }

    // ---------------------------------------------------------
    // Release inventory on checkout cancel / expiry / failure
    // ---------------------------------------------------------

    @KafkaListener(
            topics = "inventory.release.requested",
            groupId = "inventory-service"
    )
    public void handleInventoryRelease(
            InventoryReleaseRequestedEvent event, Acknowledgment acknowledgment
            ) {
        inventoryService.releaseReservedStock(
                event.checkoutId()
        );
        acknowledgment.acknowledge();
    }

    @KafkaListener(
            topics = "inventory.release.requested.order",
            groupId = "inventory-service"
    )
    public void handleInventoryReleaseForOrderService(
            Long checkoutId, Acknowledgment acknowledgment
    ) {
        inventoryService.releaseReservedStock(
                checkoutId
        );
        acknowledgment.acknowledge();
    }
}
