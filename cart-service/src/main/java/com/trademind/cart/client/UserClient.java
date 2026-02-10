package com.trademind.cart.client;

import com.trademind.cart.dto.CartSourceInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "user-client",
        url = "http://localhost:8082"
)
public interface UserClient {

    @GetMapping("/internal/user/cart-source")
    CartSourceInfoResponse getCartSourceInfo(
            @RequestParam Long sourceId,
            @RequestParam String sourceType
    );
}
