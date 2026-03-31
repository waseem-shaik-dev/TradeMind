package com.trademind.billing.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RevenueDataPointDto {

    private String label;   // date/week/month
    private String revenue;
}