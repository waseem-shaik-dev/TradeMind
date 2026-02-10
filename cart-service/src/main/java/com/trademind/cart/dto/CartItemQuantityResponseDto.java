package com.trademind.cart.dto;

public record CartItemQuantityResponseDto(
        Long cartId,
        Long cartItemId,
        Integer quantity
) {}
