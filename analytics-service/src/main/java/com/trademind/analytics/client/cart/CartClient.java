package com.trademind.analytics.client.cart;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "cart-service",
        url = "http://localhost:8091"
)
public interface CartClient {
    // future analytics (abandoned carts, etc.)
}