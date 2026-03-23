package com.trademind.analytics.service;

import com.trademind.analytics.dto.admin.AdminDashboardResponse;
import com.trademind.analytics.dto.customer.CustomerDashboardResponse;
import com.trademind.analytics.dto.merchant.MerchantDashboardResponse;
import com.trademind.analytics.dto.retailer.RetailerDashboardResponse;

public interface AnalyticsService {

    AdminDashboardResponse getAdminDashboard();

    MerchantDashboardResponse getMerchantDashboard(Long merchantId);

    RetailerDashboardResponse getRetailerDashboard(Long retailerId);

    CustomerDashboardResponse getCustomerDashboard(Long customerId);
}