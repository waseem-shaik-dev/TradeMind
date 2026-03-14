package com.trademind.inventory.dto;

import java.math.BigDecimal;

public record SellerInventoryViewResponse(

        Long inventoryId,
        Long productId,
        Long sourceId,
        String sourceRole,
        Integer availableQuantity,
        BigDecimal price

) {}