package com.trademind.analytics.kafka;

import com.trademind.analytics.entity.TopSellingProduct;
import com.trademind.analytics.repository.TopSellingProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderAnalyticsConsumer {

    private final TopSellingProductRepository repo;

    @KafkaListener(
            topics = "ORDER_PLACED_TOPIC",
            groupId = "analytics-service"
    )
    public void consume(Map<String, Object> event) {

        Long productId = Long.valueOf(event.get("productId").toString());
        Integer qty = Integer.valueOf(event.get("quantity").toString());

        TopSellingProduct product = new TopSellingProduct();
        product.setProductId(productId);
        product.setQuantitySold(qty);
        product.setPeriod(LocalDate.now());

        repo.save(product);
    }
}
