package com.trademind.order.dto;

public record OrderItemRequest(
         Long productId,
         Integer quantity
) {}
