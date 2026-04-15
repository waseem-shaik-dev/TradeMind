package com.trademind.analytics.dto.admin;

import com.trademind.analytics.dto.common.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminDashboardResponse {

    private List<MetricCardDto> metrics;

    private List<RecentActivityDto> recentActivities;

    private List<TopPerformerDto> topMerchants;

    private List<UserGraphPointDto> userGrowth;
}