package com.trademind.analytics.controller;

import com.trademind.analytics.entity.SalesReport;
import com.trademind.analytics.entity.TopSellingProduct;
import com.trademind.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService service;

    @GetMapping("/sales")
    public SalesReport sales(@RequestParam LocalDate date) {
        return service.getSalesReport(date);
    }

    @GetMapping("/top-products")
    public List<TopSellingProduct> topProducts(
            @RequestParam LocalDate period) {
        return service.getTopProducts(period);
    }
}
