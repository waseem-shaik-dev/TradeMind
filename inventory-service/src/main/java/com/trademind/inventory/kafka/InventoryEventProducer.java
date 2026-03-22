package com.trademind.inventory.kafka;

import com.trademind.events.checkout.common.EventMetadata;
import com.trademind.events.inventory.InventoryReserveFailedEvent;
import com.trademind.inventory.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String INVENTORY_RESERVE_FAILED_TOPIC =
            "inventory.reserve.failed";

    private static final String SOURCE_SERVICE = "inventory-service";
    private static final int EVENT_VERSION = 1;


    public void publishStockUpdated(Inventory inventory) {
        kafkaTemplate.send(
                "STOCK_UPDATED_TOPIC",
                inventory.getProductId().toString(),
                inventory
        );
    }

    // --------------------------------------------------------

    public void publishInventoryReserveFailed(
            Long checkoutId,
            String reason
    ) {
        InventoryReserveFailedEvent event =
                new InventoryReserveFailedEvent(
                        new EventMetadata(
                                UUID.randomUUID().toString(),
                                "INVENTORY_RESERVE_FAILED",
                                SOURCE_SERVICE,
                                LocalDateTime.now(),
                                EVENT_VERSION
                        ),
                        checkoutId,
                        reason
                );

        kafkaTemplate.send(
                INVENTORY_RESERVE_FAILED_TOPIC,
                checkoutId.toString(),
                event
        );
    }

}
