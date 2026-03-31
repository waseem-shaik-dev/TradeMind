package com.trademind.analytics.client.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminRevenueSummaryDto {

    private String totalRevenue;
    private String merchantRevenue;
    private String retailerRevenue;
    private String customerRevenue;
}