package com.trademind.analytics.client.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventorySummaryDto {

    private long totalProducts;
    private long lowStockItems;
    private long outOfStockItems;
    private String totalStockValue;
}