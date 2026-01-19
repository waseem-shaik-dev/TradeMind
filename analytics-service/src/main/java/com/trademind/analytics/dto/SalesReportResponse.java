package com.trademind.analytics.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SalesReportResponse(
        LocalDate reportDate,
        BigDecimal totalSales,
        Integer totalBills
) {}
