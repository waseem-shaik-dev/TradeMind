package com.trademind.analytics.helper;

import com.trademind.analytics.client.billing.BillingClient;
import com.trademind.analytics.client.billing.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class BillingAnalyticsHelper {

    private final BillingClient billingClient;

    public BigDecimal getRevenue(
            LocalDate start,
            LocalDate end,
            String entityType,
            Long entityId) {

        RevenueRequestDto request = RevenueRequestDto.builder()
                .startDate(start)
                .endDate(end)
                .groupBy("DAY")
                .entityType(entityType)
                .entityId(entityId)
                .build();

        return new BigDecimal(
                billingClient.getRevenue(request).getTotalRevenue()
        );
    }

    // 🔥 CURRENT vs PREVIOUS
    public BigDecimal getCurrentRevenue(int days, String entityType, Long entityId) {
        return getRevenue(LocalDate.now().minusDays(days), LocalDate.now(), entityType, entityId);
    }

    public BigDecimal getPreviousRevenue(int days, String entityType, Long entityId) {
        return getRevenue(
                LocalDate.now().minusDays(days * 2),
                LocalDate.now().minusDays(days),
                entityType,
                entityId
        );
    }
}