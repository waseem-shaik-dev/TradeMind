package com.trademind.cart.controller;

import com.trademind.cart.dto.*;
import com.trademind.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // ---------------- CART ----------------

    @GetMapping
    public UserCartListResponseDto getUserCarts(
            @RequestHeader("X-USER-ID") Long userId
    ) {
        return cartService.getUserCarts(userId);
    }

    @GetMapping("/{cartId}")
    public CartResponseDto getCart(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long cartId
    ) {
        return cartService.getCartById(userId, cartId);
    }

    // ---------------- CART ITEMS ----------------

    @PostMapping("/add")
    public CartItemQuantityResponseDto  addToCart(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestBody AddToCartRequestDto request
    ) {
        return cartService.addToCart(userId, request);
    }

    @PutMapping("/item")
    public CartItemQuantityResponseDto updateItem(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestBody UpdateCartItemRequestDto request
    ) {
        return cartService.updateCartItem(userId, request);
    }

    @DeleteMapping("/item/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long cartItemId
    ) {
        cartService.removeCartItem(userId, cartItemId);
    }


    // ---------------- CART MAINTENANCE ----------------

    @DeleteMapping("/{cartId}")
    public void deleteCart(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long cartId
    ) {
        cartService.deleteCart(userId, cartId);
    }

    @GetMapping("/{cartId}/validate")
    public CartValidationDto validateCart(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long cartId
    ) {
        return cartService.validateCart(userId, cartId);
    }

    @PutMapping("/{cartId}/checkout-lock")
    public void lockCartForCheckout(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long cartId
    ) {
        cartService.lockCartForCheckout(userId, cartId);
    }

    @PutMapping("/{cartId}/complete")
    public void markCartCompleted(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long cartId
    ) {
        cartService.markCartCompleted(userId, cartId);
    }




}
