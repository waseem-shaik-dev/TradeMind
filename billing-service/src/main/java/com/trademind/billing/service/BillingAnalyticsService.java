package com.trademind.billing.service;

import com.trademind.billing.dto.*;
import com.trademind.billing.enums.SourceType;

import java.util.List;

public interface BillingAnalyticsService {

    String getTotalRevenue();

    String getRevenueByMerchant(Long merchantId);

    String getRevenueByRetailer(Long retailerId);

    String getTotalSpentByCustomer(Long customerId);

    String getTodaySales(Long retailerId);

    List<RevenueTrendDto> getRevenueTrend(int days);

    List<TopMerchantRawDto> getTopMerchants(int limit);

    RevenueResponseDto getRevenue(RevenueRequestDto request);

    AdminRevenueSummaryDto getAdminRevenueSummary();

    List<RevenueGraphDto> getRevenueGraph(
            Long sourceId,
            SourceType sourceType,
            Long userId);
}