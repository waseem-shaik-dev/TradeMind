package com.trademind.analytics.controller;

import com.trademind.analytics.dto.admin.AdminDashboardResponse;
import com.trademind.analytics.dto.customer.CustomerDashboardResponse;
import com.trademind.analytics.dto.merchant.MerchantDashboardResponse;
import com.trademind.analytics.dto.response.ApiResponse;
import com.trademind.analytics.dto.retailer.RetailerDashboardResponse;
import com.trademind.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    // ================= ADMIN =================
    @GetMapping("/admin/dashboard")
    public ApiResponse<AdminDashboardResponse> getAdminDashboard() {
        return ApiResponse.<AdminDashboardResponse>builder()
                .success(true)
                .message("Admin dashboard fetched successfully")
                .data(analyticsService.getAdminDashboard())
                .build();
    }

    // ================= MERCHANT =================
    @GetMapping("/merchant/{merchantId}/dashboard")
    public ApiResponse<MerchantDashboardResponse> getMerchantDashboard(
            @PathVariable Long merchantId) {

        return ApiResponse.<MerchantDashboardResponse>builder()
                .success(true)
                .message("Merchant dashboard fetched successfully")
                .data(analyticsService.getMerchantDashboard(merchantId))
                .build();
    }

    // ================= RETAILER =================
    @GetMapping("/retailer/{retailerId}/dashboard")
    public ApiResponse<RetailerDashboardResponse> getRetailerDashboard(
            @PathVariable Long retailerId) {

        return ApiResponse.<RetailerDashboardResponse>builder()
                .success(true)
                .message("Retailer dashboard fetched successfully")
                .data(analyticsService.getRetailerDashboard(retailerId))
                .build();
    }

    // ================= CUSTOMER =================
    @GetMapping("/customer/{customerId}/dashboard")
    public ApiResponse<CustomerDashboardResponse> getCustomerDashboard(
            @PathVariable Long customerId) {

        return ApiResponse.<CustomerDashboardResponse>builder()
                .success(true)
                .message("Customer dashboard fetched successfully")
                .data(analyticsService.getCustomerDashboard(customerId))
                .build();
    }
}