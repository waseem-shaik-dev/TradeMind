package com.trademind.analytics.dto.merchant;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPerformanceDto {

    private String productName;
    private int totalOrders;
    private String revenue;
}