package com.trademind.analytics.client.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RevenueRequestDto {

    private LocalDate startDate;
    private LocalDate endDate;

    private String groupBy; // DAY / WEEK / MONTH

    private String entityType; // MERCHANT / RETAILER / CUSTOMER
    private Long entityId;
}