package com.trademind.merchantorder.dto;

public record OrderItemRequest(
        Long productId,
        Integer quantity
) {}
