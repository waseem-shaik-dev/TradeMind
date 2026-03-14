package com.trademind.catalogue.dto;

public record CatalogueInventoryResponse(
        Long productId,
        Integer quantityAvailable,
        boolean outOfStock
) {}
