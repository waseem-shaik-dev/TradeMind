package com.trademind.catalogue.dto;

import com.trademind.catalogue.enums.OwnerType;

import java.math.BigDecimal;


public record ProductSummaryResponse(
        Long id,
        String name,
        String sku,
        BigDecimal currentPrice,
        String primaryImageUrl
) {}
