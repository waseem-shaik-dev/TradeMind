package com.trademind.product.dto;

import com.trademind.product.enums.OwnerType;

import java.math.BigDecimal;


public record ProductSummaryResponse(
        Long id,
        String name,
        String sku,
        BigDecimal currentPrice,
        String primaryImageUrl,
        Long ownerId,
        OwnerType ownerType
) {}
