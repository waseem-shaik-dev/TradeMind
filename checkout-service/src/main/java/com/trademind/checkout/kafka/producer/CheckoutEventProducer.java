package com.trademind.checkout.kafka.producer;

import com.trademind.checkout.entity.CheckoutItem;
import com.trademind.checkout.entity.CheckoutSession;
import com.trademind.events.checkout.*;
import com.trademind.events.checkout.common.*;
import com.trademind.events.order.*;
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
    private static final String ORDER_CREATE_TOPIC = "order.create.requested";


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

    public void publishOrderCreationRequestedEvent(
            CheckoutSession session
    ) {

        OrderCreationRequestedEvent event =
                new OrderCreationRequestedEvent(
                        metadata("ORDER_CREATE_REQUESTED"),

                        session.getId(),
                        session.getCartId(),
                        session.getUserId(),
                        session.getBuyerType().name(),

                        session.getSourceId(),
                        session.getSourceType().name(),

                        DeliveryType.valueOf(session.getDeliveryType().name()),

                        mapAddress(session),

                        session.getSubtotalAmount(),
                        session.getTaxAmount(),
                        session.getDiscountAmount(),
                        session.getDeliveryFee(),
                        session.getGrandTotal(),
                        session.getCurrency(),

                        PaymentMethod.valueOf(session.getPaymentSnapshot().getPaymentMethod().name()),
                        PaymentStatus.valueOf(session.getPaymentSnapshot().getStatus().name()),

                        mapOrderItems(session),

                        LocalDateTime.now()
                );

        kafkaTemplate.send(
                ORDER_CREATE_TOPIC,
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

    private List<OrderItemDto> mapOrderItems(CheckoutSession session) {
        return session.getItems().stream()
                .map(i -> new OrderItemDto(
                        i.getProductId(),
                        i.getProductName(),
                        i.getSku(),
                        i.getImageUrl(),
                        i.getUnitPrice(),
                        i.getQuantity(),
                        i.getTotalPrice()
                ))
                .toList();
    }

    private OrderAddressDto mapAddress(CheckoutSession session) {

        var a = session.getAddressSnapshot();

        return new OrderAddressDto(
                a.getFullName(),
                a.getPhone(),
                a.getAddressLine1(),
                a.getAddressLine2(),
                a.getCity(),
                a.getState(),
                a.getPostalCode(),
                a.getCountry()
        );
    }


}
