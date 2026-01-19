package com.trademind.billing.dto;

public record BillItemRequest(
        Long productId,
        Integer quantity
) {}
