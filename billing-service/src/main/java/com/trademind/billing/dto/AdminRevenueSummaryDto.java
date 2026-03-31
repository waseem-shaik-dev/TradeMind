package com.trademind.billing.dto;

import lombok.*;

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