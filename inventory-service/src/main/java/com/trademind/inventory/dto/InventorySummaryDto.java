package com.trademind.inventory.dto;

import lombok.*;

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