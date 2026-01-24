package com.trademind.catalogue.dto;

import java.math.BigDecimal;

public record CatalogueProductSummaryResponse(
        Long productId,
        String name,
        String sku,
        BigDecimal price,
        String imageUrl,
        Integer quantityAvailable,
        boolean outOfStock
) {}

