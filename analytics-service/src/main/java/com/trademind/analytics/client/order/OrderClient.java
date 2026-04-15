package com.trademind.analytics.client.order;

import com.trademind.analytics.client.order.dto.OrderCountResponse;
import com.trademind.analytics.client.order.dto.OrderGraphDto;
import com.trademind.analytics.client.order.dto.OrderSummaryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@FeignClient(
        name = "order-service",
        url = "http://localhost:8087",
        configuration = com.trademind.analytics.client.config.FeignConfig.class
)
public interface OrderClient {

    @GetMapping("/api/orders/count")
    OrderCountResponse getTotalOrders();

    @GetMapping("/api/orders/count/by-merchant")
    OrderCountResponse getOrdersByMerchant(@RequestParam Long merchantId);

    @GetMapping("/api/orders/count/by-retailer")
    OrderCountResponse getOrdersByRetailer(@RequestParam Long retailerId);

    @GetMapping("/api/orders/recent")
    List<OrderSummaryResponse> getRecentOrders(@RequestParam Long userId);

    @GetMapping("/api/orders/recent/seller")
    List<OrderSummaryResponse> getRecentOrdersForSeller(@RequestParam Long sellerId);

    @GetMapping("/api/orders/active")
    Long getActiveOrders(@RequestParam Long customerId);

    @GetMapping("/api/orders/count/by-customer")
    OrderCountResponse getOrdersByCustomer(@RequestParam Long customerId);

    @GetMapping("/api/orders/revenue/by-customer")
    String getRevenueByCustomer(@RequestParam Long customerId);

    @GetMapping("/api/orders/count/range")
    OrderCountResponse getOrdersBetween(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end
    );

    @GetMapping("/api/orders/count/by-merchant/range")
    OrderCountResponse getMerchantOrdersBetween(
            @RequestParam Long merchantId,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end
    );

    @GetMapping("/api/orders/count/by-retailer/range")
    OrderCountResponse getRetailerOrdersBetween(
            @RequestParam Long RetailerId,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end
    );

    @GetMapping("/api/orders/count/by-customer/range")
    OrderCountResponse getCustomerOrdersBetween(
            @RequestParam Long customerId,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end
    );

    @GetMapping("/api/orders/graph")
    List<OrderGraphDto> getOrderGraph(
            @RequestParam(required = false) Long sourceId,
            @RequestParam(required = false) Long userId
    );

}