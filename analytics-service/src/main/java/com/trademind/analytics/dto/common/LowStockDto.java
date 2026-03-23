package com.trademind.analytics.dto.common;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LowStockDto {

    private String productName;
    private String sku;
    private int remaining;
    private int minRequired;
}