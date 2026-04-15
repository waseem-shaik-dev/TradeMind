package com.trademind.order.kafka.consumer;

import com.trademind.events.notification.enums.NotificationType;
import com.trademind.events.order.OrderCreationRequestedEvent;
import com.trademind.notification.sdk.annotation.Notify;
import com.trademind.order.entity.Order;
import com.trademind.order.mapper.OrderMapper;
import com.trademind.order.mapper.SellerSnapshotMapper;
import com.trademind.order.repository.OrderRepository;
import com.trademind.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCreationConsumer {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderService orderService;

    @KafkaListener(
            topics = "order.create.requested",
            groupId = "order-service-group"
    )
    @Transactional
    public void handleOrderCreation(OrderCreationRequestedEvent event, Acknowledgment ack) {

        try {

            log.info("Received OrderCreationRequestedEvent for checkoutId={}", event.checkoutId());

            // -------------------------------
            // Idempotency check
            // -------------------------------
            if (orderRepository.existsByCheckoutId(event.checkoutId())) {
                log.info("Order already exists for checkoutId={}, skipping", event.checkoutId());
                ack.acknowledge();
                return;
            }

            // -------------------------------
            // Create order aggregate
            // -------------------------------
            Order order = orderMapper.fromOrderCreationEvent(event);

            log.info("OrderService class: {}", orderService.getClass());

            orderService.saveOrderWithNotification(order);

            log.info("Order created successfully for checkoutId={}, orderId={}",
                    event.checkoutId(),
                    order.getId());

            ack.acknowledge();

        } catch (Exception e) {
            log.error("Error processing order event for checkoutId={}", event.checkoutId(), e);
            ack.acknowledge();
        }
    }
}
