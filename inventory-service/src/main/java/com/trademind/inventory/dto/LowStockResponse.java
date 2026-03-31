package com.trademind.inventory.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LowStockResponse {

    private String productName;
    private String sku;
    private int remaining;
    private int minRequired;
}