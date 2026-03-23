package com.trademind.analytics.serviceImpl;

import com.trademind.analytics.aggregator.admin.AdminDashboardAggregator;
import com.trademind.analytics.aggregator.customer.CustomerDashboardAggregator;
import com.trademind.analytics.aggregator.merchant.MerchantDashboardAggregator;
import com.trademind.analytics.aggregator.retailer.RetailerDashboardAggregator;
import com.trademind.analytics.dto.admin.AdminDashboardResponse;
import com.trademind.analytics.dto.customer.CustomerDashboardResponse;
import com.trademind.analytics.dto.merchant.MerchantDashboardResponse;
import com.trademind.analytics.dto.retailer.RetailerDashboardResponse;
import com.trademind.analytics.service.AnalyticsService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final AdminDashboardAggregator adminAggregator;
    private final MerchantDashboardAggregator merchantAggregator;
    private final RetailerDashboardAggregator retailerAggregator;
    private final CustomerDashboardAggregator customerAggregator;

    // ================= ADMIN =================
    @Override
    @Cacheable(value = "adminDashboard", key = "'admin'")
    @CircuitBreaker(name = "analyticsService", fallbackMethod = "adminFallback")
    public AdminDashboardResponse getAdminDashboard() {
        return adminAggregator.getDashboard();
    }

    public AdminDashboardResponse adminFallback(Throwable t) {
        return AdminDashboardResponse.builder()
                .metrics(java.util.List.of())
                .recentActivities(java.util.List.of())
                .topMerchants(java.util.List.of())
                .build();
    }

    // ================= MERCHANT =================
    @Override
    @Cacheable(value = "merchantDashboard", key = "#merchantId")
    @CircuitBreaker(name = "analyticsService", fallbackMethod = "merchantFallback")
    public MerchantDashboardResponse getMerchantDashboard(Long merchantId) {
        return merchantAggregator.getDashboard(merchantId);
    }

    public MerchantDashboardResponse merchantFallback(Long merchantId, Throwable t) {
        return MerchantDashboardResponse.builder()
                .metrics(java.util.List.of())
                .recentOrders(java.util.List.of())
                .lowStockAlerts(java.util.List.of())
                .build();
    }

    // ================= RETAILER =================
    @Override
    @Cacheable(value = "retailerDashboard", key = "#retailerId")
    @CircuitBreaker(name = "analyticsService", fallbackMethod = "retailerFallback")
    public RetailerDashboardResponse getRetailerDashboard(Long retailerId) {
        return retailerAggregator.getDashboard(retailerId);
    }

    public RetailerDashboardResponse retailerFallback(Long retailerId, Throwable t) {
        return RetailerDashboardResponse.builder()
                .metrics(java.util.List.of())
                .recentSales(java.util.List.of())
                .quickStats(null)
                .build();
    }

    // ================= CUSTOMER =================
    @Override
    @Cacheable(value = "customerDashboard", key = "#customerId")
    @CircuitBreaker(name = "analyticsService", fallbackMethod = "customerFallback")
    public CustomerDashboardResponse getCustomerDashboard(Long customerId) {
        return customerAggregator.getDashboard(customerId);
    }

    public CustomerDashboardResponse customerFallback(Long customerId, Throwable t) {
        return CustomerDashboardResponse.builder()
                .metrics(java.util.List.of())
                .recentOrders(java.util.List.of())
                .build();
    }
}