package com.trademind.product.dto.internal;

import java.math.BigDecimal;
import java.util.List;

public record CatalogueProductForCartResponse(
        Long productId,
        String name,
        String sku,
        BigDecimal price,
        List<String> images,
        Long sourceId,
        String sourceType
) {}
