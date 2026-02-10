package com.trademind.cart.service;

import com.trademind.cart.dto.*;

public interface CartService {

    // ---------- CART ----------

    UserCartListResponseDto getUserCarts(Long userId);

    CartResponseDto getCartById(Long userId, Long cartId);

    CartResponseDto getActiveCartBySource(
            Long userId,
            Long sourceId,
            String sourceType
    );

    void clearCart(Long userId, Long cartId);

    void deleteCart(Long userId, Long cartId);

    // ---------- CART ITEMS ----------

    CartItemQuantityResponseDto  addToCart(Long userId, AddToCartRequestDto request);

    CartItemQuantityResponseDto updateCartItem(Long userId, UpdateCartItemRequestDto request);

    void removeCartItem(Long userId, Long cartItemId);

    // ---------- VALIDATION ----------

    CartValidationDto validateCart(Long userId, Long cartId);

    // 🔒 NEW
    void lockCartForCheckout(Long userId, Long cartId);

    // ✅ NEW
    void markCartCompleted(Long userId, Long cartId);

    // 🔓 NEW (Kafka driven)
    void unlockCartAfterCheckoutFailure(Long cartId);
}
