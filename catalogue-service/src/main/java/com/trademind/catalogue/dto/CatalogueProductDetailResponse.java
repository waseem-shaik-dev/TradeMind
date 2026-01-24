package com.trademind.catalogue.dto;

import java.math.BigDecimal;
import java.util.List;

public record CatalogueProductDetailResponse(
        Long productId,
        String name,
        String sku,
        String description,

        BigDecimal price,
        boolean returnable,
        boolean taxable,

        List<String> images,
        List<ProductAttributeResponse> attributes,

        Integer quantityAvailable,
        Integer reservedQuantity,
        boolean outOfStock
) {}
