package com.trademind.analytics.service;

import com.trademind.analytics.entity.SalesReport;
import com.trademind.analytics.entity.TopSellingProduct;

import java.time.LocalDate;
import java.util.List;

public interface AnalyticsService {

    SalesReport getSalesReport(LocalDate date);

    List<TopSellingProduct> getTopProducts(LocalDate period);
}

