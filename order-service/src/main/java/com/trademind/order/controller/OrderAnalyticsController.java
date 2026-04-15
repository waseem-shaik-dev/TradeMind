package com.trademind.order.controller;

import com.trademind.order.dto.response.OrderCountResponse;
import com.trademind.order.dto.response.OrderGraphDto;
import com.trademind.order.dto.response.RecentOrderDto;
import com.trademind.order.service.OrderAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderAnalyticsController {

    private final OrderAnalyticsService service;

    @GetMapping("/count")
    public OrderCountResponse getGlobalStats() {
        return service.getGlobalStats();
    }

    @GetMapping("/count/by-merchant")
    public OrderCountResponse getMerchantStats(@RequestParam Long merchantId) {
        return service.getMerchantStats(merchantId);
    }

    @GetMapping("/count/by-retailer")
    public OrderCountResponse getRetailerStats(@RequestParam Long retailerId) {
        return service.getRetailerStats(retailerId);
    }

    @GetMapping("/count/by-customer")
    public OrderCountResponse getCustomerOrders(@RequestParam Long customerId) {
        return service.getCustomerOrderCount(customerId);
    }

    @GetMapping("/active")
    public long getActiveOrders(@RequestParam Long customerId) {
        return service.getActiveOrders(customerId);
    }

    @GetMapping("/recent/seller")
    public List<RecentOrderDto> getRecentOrdersForSeller(@RequestParam Long sellerId) {
        return service.getRecentOrdersForSeller(sellerId);
    }

    @GetMapping("/graph")
    public List<OrderGraphDto> getOrderGraph(
            @RequestParam(required = false) Long sourceId,
            @RequestParam(required = false) Long userId) {

        return service.getOrderGraph(sourceId, userId);
    }

    @GetMapping("/recent")
    public List<RecentOrderDto> getRecentOrders(@RequestParam Long userId) {
        return service.getRecentOrders(userId);
    }

    @GetMapping("/count/range")
    public OrderCountResponse getOrdersBetween(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {

        return service.getOrdersBetween(start, end);
    }

    @GetMapping("/count/by-merchant/range")
    public OrderCountResponse getMerchantOrdersBetween(
            @RequestParam Long merchantId,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {

        return service.getMerchantOrdersBetween(merchantId, start, end);
    }

    @GetMapping("/count/by-retailer/range")
    public OrderCountResponse getRetailerOrdersBetween(
            @RequestParam Long RetailerId,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {

        return service.getRetailerOrdersBetween(RetailerId, start, end);
    }

    @GetMapping("/count/by-customer/range")
    public OrderCountResponse getCustomerOrdersBetween(
            @RequestParam Long customerId,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {

        return service.getCustomerOrdersBetween(customerId, start, end);
    }
}
