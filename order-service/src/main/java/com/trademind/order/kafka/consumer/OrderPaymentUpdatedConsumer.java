package com.trademind.order.kafka.consumer;

import com.trademind.events.order.OrderPaymentUpdatedEvent;
import com.trademind.order.entity.Order;
import com.trademind.order.enums.OrderStatus;
import com.trademind.order.enums.PaymentStatus;
import com.trademind.order.kafka.producer.BillingEventProducer;
import com.trademind.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderPaymentUpdatedConsumer {

    private final OrderRepository orderRepository;
    private final BillingEventProducer billingEventProducer;

    @KafkaListener(
            topics = "order.payment.updated",
            groupId = "order-service-group"
    )
    @Transactional
    public void handlePaymentUpdate(OrderPaymentUpdatedEvent event) {

        log.info("Received OrderPaymentUpdatedEvent for checkoutId={}",
                event.checkoutId());

        Order order = orderRepository.findByCheckoutId(event.checkoutId())
                .orElseThrow(() -> new IllegalStateException(
                        "Order not found for checkoutId=" + event.checkoutId()
                ));

        // Event module enum
        com.trademind.events.order.PaymentStatus eventStatus =
                event.paymentStatus();

        // Convert event enum → order enum
        com.trademind.order.enums.PaymentStatus orderPaymentStatus =
                com.trademind.order.enums.PaymentStatus.valueOf(
                        eventStatus.name()
                );

        switch (eventStatus) {

            case PAID -> {

                if (order.getPaymentStatus() ==
                        com.trademind.order.enums.PaymentStatus.PAID) {
                    return; // idempotent
                }

                order.setPaymentStatus(PaymentStatus.PAID);
                order.setOrderStatus(OrderStatus.AWAITING_ACCEPTANCE);

                //  TRIGGER BILLING
                billingEventProducer.publishBillingEvent(order);

                log.info("Order {} marked as CONFIRMED (payment successful)",
                        order.getId());
            }

            case FAILED -> {

                if (order.getPaymentStatus() ==
                        com.trademind.order.enums.PaymentStatus.FAILED) {
                    return;
                }

                order.setPaymentStatus(PaymentStatus.FAILED);
                order.setOrderStatus(OrderStatus.FAILED);

                log.info("Order {} marked as FAILED (payment failed)",
                        order.getId());
            }

            case INITIATED -> {
                order.setPaymentStatus(orderPaymentStatus);
            }

            case PENDING -> {
                order.setPaymentStatus(orderPaymentStatus);
            }
        }
    }
}
