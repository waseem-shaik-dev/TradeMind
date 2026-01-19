package com.trademind.inventory.kafka;

import com.trademind.inventory.entity.StockItem;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishStockUpdated(StockItem stock) {
        kafkaTemplate.send(
                "STOCK_UPDATED_TOPIC",
                stock.getProductId().toString(),
                stock
        );
    }
}
