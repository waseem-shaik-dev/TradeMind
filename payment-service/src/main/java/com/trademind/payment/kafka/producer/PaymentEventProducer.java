package com.trademind.payment.kafka.producer;

import com.trademind.events.checkout.common.EventMetadata;
import com.trademind.events.order.OrderPaymentUpdatedEvent;
import com.trademind.events.payment.*;
import com.trademind.payment.entity.PaymentTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    // ---- Topics ----
    private static final String PAYMENT_CREATED_TOPIC   = "payment.created";
    private static final String PAYMENT_INITIATED_TOPIC = "payment.initiated";
    private static final String PAYMENT_SUCCESS_TOPIC   = "payment.success";
    private static final String PAYMENT_FAILED_TOPIC    = "payment.failed";
    private static final String PAYMENT_CANCELLED_TOPIC = "payment.cancelled";
    private static final String ORDER_PAYMENT_TOPIC = "order.payment.updated";
    private static final String SOURCE_SERVICE = "payment-service";
    private static final int EVENT_VERSION = 1;



    // -------------------------------------------------

    public void publishPaymentCreated(PaymentTransaction payment) {

        PaymentCreatedEvent event =
                new PaymentCreatedEvent(
                        UUID.randomUUID().toString(),
                        payment.getId(),
                        payment.getCheckoutId(),
                        payment.getUserId(),
                        payment.getAmount(),
                        payment.getCurrency(),
                        payment.getPaymentMethod().name(),
                        payment.getPaymentProvider().name(),
                        LocalDateTime.now()
                );

        kafkaTemplate.send(
                PAYMENT_CREATED_TOPIC,
                payment.getCheckoutId().toString(),
                event
        );
    }

    public void publishPaymentInitiated(PaymentTransaction payment) {

        PaymentInitiatedEvent event =
                new PaymentInitiatedEvent(
                        UUID.randomUUID().toString(),
                        payment.getId(),
                        payment.getCheckoutId(),
                        payment.getPaymentProvider().name(),
                        payment.getProviderPaymentIntentId(),
                        LocalDateTime.now()
                );

        kafkaTemplate.send(
                PAYMENT_INITIATED_TOPIC,
                payment.getCheckoutId().toString(),
                event
        );
    }

    public void publishPaymentSuccess(PaymentTransaction payment) {

        PaymentSuccessEvent event =
                new PaymentSuccessEvent(
                        UUID.randomUUID().toString(),
                        payment.getId(),
                        payment.getCheckoutId(),
                        payment.getUserId(),
                        payment.getAmount(),
                        payment.getCurrency(),
                        payment.getPaymentProvider().name(),
                        payment.getProviderPaymentIntentId(),
                        LocalDateTime.now()
                );

        kafkaTemplate.send(
                PAYMENT_SUCCESS_TOPIC,
                payment.getCheckoutId().toString(),
                event
        );
    }

    public void publishPaymentFailure(PaymentTransaction payment) {

        PaymentFailedEvent event =
                new PaymentFailedEvent(
                        UUID.randomUUID().toString(),
                        payment.getId(),
                        payment.getCheckoutId(),
                        payment.getUserId(),
                        payment.getFailureReason(),
                        payment.getPaymentProvider().name(),
                        LocalDateTime.now()
                );

        kafkaTemplate.send(
                PAYMENT_FAILED_TOPIC,
                payment.getCheckoutId().toString(),
                event
        );
    }

    public void publishPaymentCancelled(PaymentTransaction payment, String reason) {

        PaymentCancelledEvent event =
                new PaymentCancelledEvent(
                        UUID.randomUUID().toString(),
                        payment.getId(),
                        payment.getCheckoutId(),
                        payment.getUserId(),
                        reason,
                        LocalDateTime.now()
                );

        kafkaTemplate.send(
                PAYMENT_CANCELLED_TOPIC,
                payment.getCheckoutId().toString(),
                event
        );
    }

    public void publishOrderPaymentUpdated(
            Long checkoutId,
            com.trademind.events.order.PaymentStatus paymentStatus,
            String transactionId
    ) {

        OrderPaymentUpdatedEvent event =
                new OrderPaymentUpdatedEvent(
                        metadata("ORDER_PAYMENT_UPDATED"),
                        checkoutId,
                        null,
                        paymentStatus,
                        transactionId,
                        LocalDateTime.now()
                );

        kafkaTemplate.send(
                ORDER_PAYMENT_TOPIC,
                checkoutId.toString(),
                event
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

}
