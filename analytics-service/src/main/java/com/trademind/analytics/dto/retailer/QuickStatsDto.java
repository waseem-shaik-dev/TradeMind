package com.trademind.analytics.dto.retailer;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuickStatsDto {

    private String weeklySales;
    private String monthlySales;
    private int customers;
    private int lowStockItems;
}