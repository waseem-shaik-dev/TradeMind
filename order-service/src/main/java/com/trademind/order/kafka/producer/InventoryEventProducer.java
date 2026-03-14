package com.trademind.order.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryEventProducer {

    private final KafkaTemplate<String, Long> kafkaTemplate;

    private static final String TOPIC =
            "inventory.release.requested.order";

    public void publishInventoryRelease(Long checkoutId) {

        kafkaTemplate.send(TOPIC, checkoutId);

    }
}
