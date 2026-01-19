package com.trademind.analytics.kafka;

import com.trademind.analytics.entity.InventoryReport;
import com.trademind.analytics.repository.InventoryReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InventoryAnalyticsConsumer {

    private final InventoryReportRepository repo;

    @KafkaListener(
            topics = "LOW_STOCK_TOPIC",
            groupId = "analytics-service"
    )
    public void consume(Map<String, Object> event) {

        InventoryReport report = new InventoryReport();
        report.setReportDate(LocalDate.now());
        report.setLowStockCount(1);
        report.setOutOfStockCount(0);

        repo.save(report);
    }
}
