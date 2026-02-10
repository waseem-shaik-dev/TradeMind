package com.trademind.checkout.kafka.producer;

import com.trademind.checkout.entity.CheckoutItem;
import com.trademind.checkout.entity.CheckoutSession;
import com.trademind.events.checkout.*;
import com.trademind.events.checkout.common.*;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CheckoutEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    // ---------- Topics ----------
    private static final String INVENTORY_RESERVE_TOPIC = "inventory.reserve.requested";
    private static final String INVENTORY_RELEASE_TOPIC = "inventory.release.requested";
    private static final String CHECKOUT_CONFIRMED_TOPIC = "checkout.confirmed";
    private static final String CHECKOUT_CANCELLED_TOPIC = "checkout.cancelled";

    private static final String SOURCE_SERVICE = "checkout-service";
    private static final int EVENT_VERSION = 1;

    // ----------------------------------------------------------

    public void publishInventoryReserveEvent(CheckoutSession session) {

        InventoryReserveRequestedEvent event =
                new InventoryReserveRequestedEvent(
                        metadata("INVENTORY_RESERVE_REQUESTED"),
                        session.getId(),
                        session.getUserId(),
                        mapItems(session.getItems())
                );

        kafkaTemplate.send(
                INVENTORY_RESERVE_TOPIC,
                session.getId().toString(),
                event
        );
    }

    public void publishInventoryReleaseEvent(
            CheckoutSession session,
            String reason
    ) {
        InventoryReleaseRequestedEvent event =
                new InventoryReleaseRequestedEvent(
                        metadata("INVENTORY_RELEASE_REQUESTED"),
                        session.getId(),
                        mapItems(session.getItems()),
                        reason
                );

        kafkaTemplate.send(
                INVENTORY_RELEASE_TOPIC,
                session.getId().toString(),
                event
        );
    }

    public void publishCheckoutConfirmedEvent(CheckoutSession session) {

        CheckoutConfirmedEvent event =
                new CheckoutConfirmedEvent(
                        metadata("CHECKOUT_CONFIRMED"),
                        session.getId(),
                        session.getUserId(),
                        session.getPaymentSnapshot().getPaymentMethod().name(),
                        session.getCurrency()
                );

        kafkaTemplate.send(
                CHECKOUT_CONFIRMED_TOPIC,
                session.getId().toString(),
                event
        );
    }

    public void publishCheckoutCancelledEvent(
            CheckoutSession session,
            String reason
    ) {
        CheckoutCancelledEvent event =
                new CheckoutCancelledEvent(
                        metadata("CHECKOUT_CANCELLED"),
                        session.getId(),
                        session.getUserId(),
                        reason
                );

        kafkaTemplate.send(
                CHECKOUT_CANCELLED_TOPIC,
                session.getId().toString(),
                event
        );
    }

    // ---------- Helpers ----------

    private EventMetadata metadata(String eventType) {
        return new EventMetadata(
                UUID.randomUUID().toString(),
                eventType,
                SOURCE_SERVICE,
                LocalDateTime.now(),
                EVENT_VERSION
        );
    }

    private List<ItemQuantityDto> mapItems(List<CheckoutItem> items) {
        return items.stream()
                .map(i -> new ItemQuantityDto(
                        i.getProductId(),
                        i.getQuantity()
                ))
                .toList();
    }
}
