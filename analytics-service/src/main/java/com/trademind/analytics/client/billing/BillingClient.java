package com.trademind.analytics.client.billing;

import com.trademind.analytics.client.billing.dto.AdminRevenueSummaryDto;
import com.trademind.analytics.client.billing.dto.RevenueRequestDto;
import com.trademind.analytics.client.billing.dto.RevenueResponseDto;
import com.trademind.analytics.client.billing.dto.TopMerchantRawDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "billing-service",
        url = "http://localhost:8085",
        configuration = com.trademind.analytics.client.config.FeignConfig.class
)
public interface BillingClient {

    // 🔥 CORE DYNAMIC API (PRIMARY)
    @PostMapping("/api/billing/revenue")
    RevenueResponseDto getRevenue(RevenueRequestDto request);

    // 🔥 TOP PERFORMERS
    @GetMapping("/api/billing/top-merchants")
    List<TopMerchantRawDto> getTopMerchants(@RequestParam int limit);

    // ⚡ OPTIONAL (keep only if needed)
    @GetMapping("/api/billing/today-sales")
    String getTodaySales(@RequestParam Long retailerId);

    @GetMapping("/api/billing/admin/summary")
    AdminRevenueSummaryDto getAdminSummary();
}