package com.trademind.order.kafka.producer;

import com.trademind.events.checkout.common.EventMetadata;
import com.trademind.events.order.OrderStatusUpdatedEvent;
import com.trademind.order.entity.Order;
import com.trademind.order.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    // -------------------------------
    // Topics
    // -------------------------------
    private static final String ORDER_CREATED_TOPIC = "order.created";
    private static final String ORDER_CONFIRMED_TOPIC = "order.confirmed";
    private static final String ORDER_STATUS_UPDATED_TOPIC = "order.status.updated";
    private static final String ORDER_CANCELLED_TOPIC = "order.cancelled";
    private static final String ORDER_COD_PAYMENT_RECEIVED_TOPIC = "order.cod.payment.received";

    private static final String SOURCE_SERVICE = "order-service";
    private static final int EVENT_VERSION = 1;

    // ============================================================
    // 1️⃣ Order Created
    // ============================================================

    public void publishOrderCreated(Order order) {

        kafkaTemplate.send(
                ORDER_CREATED_TOPIC,
                order.getId().toString(),
                buildStatusEvent(order, "ORDER_CREATED")
        );
    }

    // ============================================================
    // 2️⃣ Order Confirmed
    // ============================================================

    public void publishOrderConfirmed(Order order) {

        kafkaTemplate.send(
                ORDER_CONFIRMED_TOPIC,
                order.getId().toString(),
                buildStatusEvent(order, "ORDER_CONFIRMED")
        );
    }

    // ============================================================
    // 3️⃣ Generic Status Update
    // ============================================================

    public void publishOrderStatusUpdated(Order order, String reason) {

        kafkaTemplate.send(
                ORDER_STATUS_UPDATED_TOPIC,
                order.getId().toString(),
                buildStatusEvent(order, reason)
        );
    }

    // ============================================================
    // 4️⃣ Order Cancelled
    // ============================================================

    public void publishOrderCancelled(Order order, String reason) {

        kafkaTemplate.send(
                ORDER_CANCELLED_TOPIC,
                order.getId().toString(),
                buildStatusEvent(order, reason)
        );
    }

    // ============================================================
    // 5️⃣ COD Payment Received
    // ============================================================

    public void publishCodPaymentReceived(
            Order order,
            String transactionReference
    ) {

        kafkaTemplate.send(
                ORDER_COD_PAYMENT_RECEIVED_TOPIC,
                order.getId().toString(),
                new CodPaymentReceivedEvent(
                        metadata("ORDER_COD_PAYMENT_RECEIVED"),
                        order.getCheckoutId(),
                        order.getId(),
                        transactionReference,
                        LocalDateTime.now()
                )
        );
    }

    // ============================================================
    // Helper: Build OrderStatusUpdatedEvent
    // ============================================================

    private OrderStatusUpdatedEvent buildStatusEvent(
            Order order,
            String reason
    ) {

        return new OrderStatusUpdatedEvent(
                metadata("ORDER_STATUS_UPDATED"),
                order.getId(),
                mapStatus(order.getOrderStatus()),
                reason,
                LocalDateTime.now()
        );
    }

    private com.trademind.events.order.OrderStatus mapStatus(
            OrderStatus status
    ) {
        return com.trademind.events.order.OrderStatus.valueOf(
                status.name()
        );
    }

    private EventMetadata metadata(String eventType) {
        return new EventMetadata(
                UUID.randomUUID().toString(),
                eventType,
                SOURCE_SERVICE,
                LocalDateTime.now(),
                EVENT_VERSION
        );
    }

    // ============================================================
    // Internal Event for COD
    // ============================================================

    public record CodPaymentReceivedEvent(
            EventMetadata metadata,
            Long checkoutId,
            Long orderId,
            String transactionReference,
            LocalDateTime receivedAt
    ) {}
}
