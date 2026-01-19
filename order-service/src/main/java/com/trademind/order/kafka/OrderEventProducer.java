package com.trademind.order.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishStockReserve(Long productId, Integer qty, Long orderId) {
        kafkaTemplate.send(
                "STOCK_RESERVE_TOPIC",
                productId.toString(),
                Map.of(
                        "productId", productId,
                        "quantity", qty,
                        "orderId", orderId
                )
        );
    }
}

