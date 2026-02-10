package com.trademind.catalogue.dto;

import com.trademind.catalogue.enums.OwnerType;

import java.math.BigDecimal;

public record CatalogueProductSummaryResponse(
        Long productId,
        String name,
        String sku,
        BigDecimal price,
        String imageUrl,
        Integer quantityAvailable,
        boolean outOfStock,
        Long ownerId,
        OwnerType ownerType
) {}

