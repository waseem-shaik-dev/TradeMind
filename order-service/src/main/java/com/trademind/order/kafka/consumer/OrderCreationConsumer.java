package com.trademind.order.kafka.consumer;

import com.trademind.events.order.OrderCreationRequestedEvent;
import com.trademind.order.entity.Order;
import com.trademind.order.mapper.OrderMapper;
import com.trademind.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCreationConsumer {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @KafkaListener(
            topics = "order.create.requested",
            groupId = "order-service-group"
    )
    @Transactional
    public void handleOrderCreation(OrderCreationRequestedEvent event) {

        log.info("Received OrderCreationRequestedEvent for checkoutId={}", event.checkoutId());

        // -------------------------------
        // Idempotency check
        // -------------------------------
        if (orderRepository.existsByCheckoutId(event.checkoutId())) {
            log.info("Order already exists for checkoutId={}, skipping", event.checkoutId());
            return;
        }

        // -------------------------------
        // Create order aggregate
        // -------------------------------
        Order order = orderMapper.fromOrderCreationEvent(event);

        orderRepository.save(order);

        log.info("Order created successfully for checkoutId={}, orderId={}",
                event.checkoutId(),
                order.getId());
    }
}
