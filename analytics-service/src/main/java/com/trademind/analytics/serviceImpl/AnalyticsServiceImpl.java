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
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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
    public AdminDashboardResponse getAdminDashboard() {
        return adminAggregator.getDashboard();
    }

    // ================= MERCHANT =================
    @Override
    @Cacheable(value = "merchantDashboard", key = "#merchantId")
    public MerchantDashboardResponse getMerchantDashboard(Long merchantId) {
        return merchantAggregator.getDashboard(merchantId);
    }

    // ================= RETAILER =================
    @Override
    @Cacheable(value = "retailerDashboard", key = "#retailerId")
    public RetailerDashboardResponse getRetailerDashboard(Long retailerId) {
        return retailerAggregator.getDashboard(retailerId);
    }

    // ================= CUSTOMER =================
    @Override
    @Cacheable(value = "customerDashboard", key = "#customerId")
    public CustomerDashboardResponse getCustomerDashboard(Long customerId) {
        return customerAggregator.getDashboard(customerId);
    }
}