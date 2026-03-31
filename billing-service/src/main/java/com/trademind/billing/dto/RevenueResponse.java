package com.trademind.billing.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RevenueResponse {

    private String totalRevenue;
}