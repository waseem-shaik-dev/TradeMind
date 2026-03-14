package com.trademind.catalogue.dto;

import java.math.BigDecimal;
import java.util.List;

public record CatalogueProductForCartResponse(
        Long productId,
        String name,
        String sku,
        BigDecimal price,
        List<String> images
) {}
