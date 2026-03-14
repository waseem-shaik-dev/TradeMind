package com.trademind.inventory.dto;

import java.math.BigDecimal;

public record BulkInventoryRequest(
        Long productid,
        Integer quantity,
        BigDecimal price
) {}
