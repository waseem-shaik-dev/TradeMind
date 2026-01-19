package com.trademind.inventory.dto;

public record StockUpdateRequest(
        Long productId,
        Integer quantity,
        Integer reorderLevel,
        String referenceId,
        String movementType
) {}
