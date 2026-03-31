package com.trademind.analytics.client.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopMerchantRawDto {

    private Long merchantId;
    private Long totalOrders;
    private String revenue;
}