package com.trademind.analytics.client.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RevenueResponseDto {

    private String totalRevenue;
    private List<RevenueDataPointDto> data;
}