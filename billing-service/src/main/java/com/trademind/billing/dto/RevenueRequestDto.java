package com.trademind.billing.dto;

import lombok.*;

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