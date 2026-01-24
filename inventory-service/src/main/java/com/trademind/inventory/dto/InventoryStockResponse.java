package com.trademind.inventory.dto;

import java.time.LocalDateTime;

public record InventoryStockResponse(
        Long inventoryId,
        Long ownerId,
        String location,
        String primaryImageUrl,
        Long stockItemId,
        Long productId,
        Integer quantityAvailable,
        Integer reservedQuantity,
        boolean outOfStock,
        Integer reorderLevel,

        LocalDateTime inventoryCreatedAt
) {}
