package com.trademind.analytics.serviceImpl;

import com.trademind.analytics.entity.SalesReport;
import com.trademind.analytics.entity.TopSellingProduct;
import com.trademind.analytics.repository.SalesReportRepository;
import com.trademind.analytics.repository.TopSellingProductRepository;
import com.trademind.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final SalesReportRepository salesRepo;
    private final TopSellingProductRepository topRepo;

    @Override
    public SalesReport getSalesReport(LocalDate date) {
        return salesRepo.findByReportDate(date)
                .orElseThrow(() -> new RuntimeException("No data"));
    }

    @Override
    public List<TopSellingProduct> getTopProducts(LocalDate period) {
        return topRepo.findAll()
                .stream()
                .filter(p -> p.getPeriod().equals(period))
                .toList();
    }
}

