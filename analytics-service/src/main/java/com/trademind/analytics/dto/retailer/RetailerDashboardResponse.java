package com.trademind.analytics.dto.retailer;

import com.trademind.analytics.dto.common.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RetailerDashboardResponse {

    private List<MetricCardDto> metrics;

    private List<OrderSummaryDto> recentSales;

    private QuickStatsDto quickStats;

    private List<GraphPointDto> revenueGraph;
    private List<GraphPointDto> orderGraph;
}