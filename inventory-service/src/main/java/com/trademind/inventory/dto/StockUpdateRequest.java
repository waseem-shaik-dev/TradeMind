package com.trademind.inventory.dto;

public record StockUpdateRequest(

        Long sellerId,
        String sellerRole,

        Long productId,

        Integer quantity,
        Integer reorderLevel,

        String referenceId,
        String movementType

) {}