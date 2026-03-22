package com.trademind.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InventoryStockResponse(

        Long inventoryId,
        Long sellerId,
        String sellerRole,

        Long productId,

        Integer quantityAvailable,
        BigDecimal price,
        String productName,

        boolean outOfStock,
        Integer reorderLevel,

        String primaryImageUrl,

        LocalDateTime createdAt

) {}