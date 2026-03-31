package com.trademind.billing.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RevenueResponseDto {

    private String totalRevenue;
    private List<RevenueDataPointDto> data;
}