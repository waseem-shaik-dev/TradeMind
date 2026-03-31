package com.trademind.billing.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopMerchantRawDto {

    private Long merchantId;
    private Long totalOrders;
    private String revenue;
}