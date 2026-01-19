package com.trademind.merchantorder.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MerchantOrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishStockIn(Long productId, Integer quantity, Long orderId) {
        kafkaTemplate.send(
                "STOCK_IN_TOPIC",
                productId.toString(),
                Map.of(
                        "productId", productId,
                        "quantity", quantity,
                        "orderId", orderId
                )
        );
    }
}
