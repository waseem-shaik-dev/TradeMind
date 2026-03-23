package com.trademind.analytics.dto.admin;

import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminTrendsResponse {

    private Map<String, Double> revenueTrend;   // date -> revenue
    private Map<String, Integer> userGrowth;    // date -> users
}