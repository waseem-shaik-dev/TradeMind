package com.trademind.analytics.client.billing;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "billing-service",
        url = "http://localhost:8085",
        configuration = com.trademind.analytics.client.config.FeignConfig.class
)
public interface BillingClient {

    @GetMapping("/api/billing/total-revenue")
    String getTotalRevenue();

    @GetMapping("/api/billing/revenue/by-merchant")
    String getRevenueByMerchant(@RequestParam Long merchantId);

    @GetMapping("/api/billing/revenue/by-retailer")
    String getRevenueByRetailer(@RequestParam Long retailerId);

    @GetMapping("/api/billing/today-sales")
    String getTodaySales(@RequestParam Long retailerId);
}