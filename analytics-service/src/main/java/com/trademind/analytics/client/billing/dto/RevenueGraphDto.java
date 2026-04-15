package com.trademind.analytics.client.billing.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RevenueGraphDto {

    private String label;   // date
    private String revenue;
}