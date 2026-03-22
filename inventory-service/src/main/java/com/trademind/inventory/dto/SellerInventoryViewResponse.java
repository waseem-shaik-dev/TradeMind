package com.trademind.inventory.dto;

import java.math.BigDecimal;

public record SellerInventoryViewResponse(

        Long inventoryId,
        Long productId,

        Long sourceId,
        String sourceRole,

        String productName,

        Integer quantityAvailable,
        BigDecimal price,

        String primaryImageUrl

) {}