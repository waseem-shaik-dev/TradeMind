package com.trademind.catalogue.dto;

import com.trademind.catalogue.enums.OwnerType;

public record CatalogueInventoryResponse(
        Long productId,
        Long sourceId,
        String sourceRole,
        Integer quantityAvailable,
        boolean outOfStock
) {}
