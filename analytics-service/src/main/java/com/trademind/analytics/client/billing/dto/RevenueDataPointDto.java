package com.trademind.analytics.client.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RevenueDataPointDto {

    private String label;   // date/week/month
    private String revenue;
}