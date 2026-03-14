package com.trademind.inventory.dto;

public record CatalogueInventoryResponse(
        Long productId,
        Integer quantityAvailable,
        boolean outOfStock
) {}
