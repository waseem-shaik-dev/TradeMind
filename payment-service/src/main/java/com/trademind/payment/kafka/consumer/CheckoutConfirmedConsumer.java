package com.trademind.payment.kafka.consumer;

import com.trademind.events.checkout.CheckoutConfirmedEvent;
import com.trademind.events.order.OrderCreationRequestedEvent;
import com.trademind.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckoutConfirmedConsumer {

    private final PaymentService paymentService;


    @KafkaListener(
            topics = "order.create.requested",
            groupId = "payment-service"
    )
    public void handleOrderCreationRequested(
            OrderCreationRequestedEvent event,
            Acknowledgment acknowledgment
    ) {

        paymentService.createPaymentForOrder(event);

        acknowledgment.acknowledge();
    }

}
