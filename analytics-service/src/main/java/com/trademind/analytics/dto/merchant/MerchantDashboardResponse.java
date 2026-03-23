package com.trademind.analytics.dto.merchant;

import com.trademind.analytics.dto.common.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MerchantDashboardResponse {

    private List<MetricCardDto> metrics;

    private List<OrderSummaryDto> recentOrders;

    private List<LowStockDto> lowStockAlerts;
}