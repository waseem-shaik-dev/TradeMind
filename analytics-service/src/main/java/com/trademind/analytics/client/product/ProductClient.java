package com.trademind.analytics.client.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "product-service",
        url = "http://localhost:8083",
        configuration = com.trademind.analytics.client.config.FeignConfig.class
)
public interface ProductClient {

    @GetMapping("/api/products/count")
    long getTotalProducts();


}