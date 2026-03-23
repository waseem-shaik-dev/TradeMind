package com.trademind.analytics.dto.customer;

import com.trademind.analytics.dto.common.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDashboardResponse {

    private List<MetricCardDto> metrics;

    private List<OrderSummaryDto> recentOrders;
}