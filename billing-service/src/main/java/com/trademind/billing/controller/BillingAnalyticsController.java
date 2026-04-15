package com.trademind.billing.controller;

import com.trademind.billing.dto.*;
import com.trademind.billing.enums.SourceType;
import com.trademind.billing.service.BillingAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
public class BillingAnalyticsController {

    private final BillingAnalyticsService service;

    @GetMapping("/total-revenue")
    public String getTotalRevenue() {
        return service.getTotalRevenue();
    }

    @GetMapping("/revenue/by-merchant")
    public String getRevenueByMerchant(@RequestParam Long merchantId) {
        return service.getRevenueByMerchant(merchantId);
    }

    @GetMapping("/graph/revenue")
    public List<RevenueGraphDto> getRevenueGraph(
            @RequestParam(required = false) Long sourceId,
            @RequestParam(required = false) SourceType sourceType,
            @RequestParam(required = false) Long userId) {

        return service.getRevenueGraph(sourceId, sourceType, userId);
    }

    @GetMapping("/revenue/by-retailer")
    public String getRevenueByRetailer(@RequestParam Long retailerId) {
        return service.getRevenueByRetailer(retailerId);
    }

    @GetMapping("/revenue/by-customer")
    public String getCustomerSpent(@RequestParam Long customerId) {
        return service.getTotalSpentByCustomer(customerId);
    }

    @GetMapping("/today-sales")
    public String getTodaySales(@RequestParam Long retailerId) {
        return service.getTodaySales(retailerId);
    }

    @GetMapping("/revenue/trend")
    public List<RevenueTrendDto> getTrend(
            @RequestParam(defaultValue = "30") int days) {
        return service.getRevenueTrend(days);
    }

    @GetMapping("/top-merchants")
    public List<TopMerchantRawDto> getTopMerchants(
            @RequestParam(defaultValue = "5") int limit) {
        return service.getTopMerchants(limit);
    }

    @PostMapping("/revenue")
    public RevenueResponseDto getRevenue(
            @RequestBody RevenueRequestDto request) {

        return service.getRevenue(request);
    }

    @GetMapping("/admin/summary")
    public AdminRevenueSummaryDto getAdminSummary() {
        return service.getAdminRevenueSummary();
    }
}