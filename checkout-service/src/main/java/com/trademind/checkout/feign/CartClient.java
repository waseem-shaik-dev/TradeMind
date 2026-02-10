package com.trademind.checkout.feign;

import com.trademind.checkout.feign.config.FeignClientConfig;
import com.trademind.checkout.feign.dto.cart.CartCheckoutResponseDto;
import com.trademind.checkout.feign.dto.cart.CartResponseDto;
import com.trademind.checkout.feign.dto.cart.CartValidationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "cart-service",
        url = "http://localhost:8091",
        configuration = FeignClientConfig.class
)
public interface CartClient {

    /**
     * Used to create checkout.
     * This must return a validated, checkout-ready cart snapshot.
     */
    @GetMapping("/api/cart/{cartId}")
    CartResponseDto getCart(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable("cartId") Long cartId
    );

    /**
     * Optional but STRONGLY recommended:
     * Explicit validation endpoint before checkout.
     */
    @GetMapping("/api/cart/{cartId}/validate")
    CartValidationDto validateCart(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable("cartId") Long cartId
    );
}

