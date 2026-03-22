package com.trademind.inventory.dto;

import com.trademind.inventory.enums.OwnerType;

public record CatalogueInventoryResponse(
        Long productId,
        Long sourceId,
        String sourceRole,
        Integer quantityAvailable,
        boolean outOfStock
) {}
