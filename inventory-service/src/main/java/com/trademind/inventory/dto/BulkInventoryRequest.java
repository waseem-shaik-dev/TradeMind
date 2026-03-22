package com.trademind.inventory.dto;

import java.math.BigDecimal;

public record BulkInventoryRequest(

        Long productId,
        Integer quantity,
        String productName,
        BigDecimal price,
        String primaryImageUrl

) {}