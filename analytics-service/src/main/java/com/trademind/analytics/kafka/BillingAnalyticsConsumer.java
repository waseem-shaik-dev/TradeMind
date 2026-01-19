package com.trademind.analytics.kafka;

import com.trademind.analytics.entity.DailyMetrics;
import com.trademind.analytics.entity.SalesReport;
import com.trademind.analytics.repository.DailyMetricsRepository;
import com.trademind.analytics.repository.SalesReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BillingAnalyticsConsumer {

    private final SalesReportRepository salesRepo;
    private final DailyMetricsRepository metricsRepo;

    @KafkaListener(
            topics = "BILL_CREATED_TOPIC",
            groupId = "analytics-service"
    )
    public void consume(Map<String, Object> event) {

        LocalDate today = LocalDate.now();
        BigDecimal amount = new BigDecimal(event.get("amount").toString());

        SalesReport report = salesRepo
                .findByReportDate(today)
                .orElse(
                        SalesReport.builder()
                                .reportDate(today)
                                .totalSales(BigDecimal.ZERO)
                                .totalBills(0)
                                .build()
                );

        report.setTotalSales(report.getTotalSales().add(amount));
        report.setTotalBills(report.getTotalBills() + 1);

        salesRepo.save(report);

        DailyMetrics metrics = metricsRepo
                .findAll().stream()
                .filter(m -> m.getDate().equals(today))
                .findFirst()
                .orElse(
                        new DailyMetrics(null, today, 0, BigDecimal.ZERO)
                );

        metrics.setOrdersCount(metrics.getOrdersCount() + 1);
        metrics.setSalesAmount(metrics.getSalesAmount().add(amount));

        metricsRepo.save(metrics);
    }
}
