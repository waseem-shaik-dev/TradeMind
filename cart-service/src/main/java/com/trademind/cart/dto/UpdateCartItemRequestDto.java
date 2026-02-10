package com.trademind.cart.dto;

public record UpdateCartItemRequestDto(
        Long cartItemId,
        Integer quantity
) {
}
