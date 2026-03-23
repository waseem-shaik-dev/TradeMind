package com.trademind.analytics.client.order;

import com.trademind.analytics.client.order.dto.OrderSummaryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "order-service",
        url = "http://localhost:8087",
        configuration = com.trademind.analytics.client.config.FeignConfig.class
)
public interface OrderClient {

    @GetMapping("/api/orders/count")
    long getTotalOrders();

    @GetMapping("/api/orders/count/by-merchant")
    long getOrdersByMerchant(@RequestParam Long merchantId);

    @GetMapping("/api/orders/count/by-retailer")
    long getOrdersByRetailer(@RequestParam Long retailerId);

    @GetMapping("/api/orders/recent")
    List<OrderSummaryResponse> getRecentOrders(@RequestParam Long userId);

    @GetMapping("/api/orders/active")
    long getActiveOrders(@RequestParam Long customerId);
}