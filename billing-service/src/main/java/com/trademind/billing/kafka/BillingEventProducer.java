package com.trademind.billing.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BillingEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishStockOut(Long productId, Integer qty, Long billId) {
        kafkaTemplate.send(
                "STOCK_OUT_TOPIC",
                productId.toString(),
                Map.of(
                        "productId", productId,
                        "quantity", qty,
                        "billId", billId
                )
        );
    }
}
