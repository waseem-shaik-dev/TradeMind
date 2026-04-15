package com.trademind.order.service;

import com.trademind.order.dto.response.OrderCountResponse;
import com.trademind.order.dto.response.OrderGraphDto;
import com.trademind.order.dto.response.RecentOrderDto;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderAnalyticsService {

    OrderCountResponse getGlobalStats();

    OrderCountResponse getMerchantStats(Long merchantId);

    OrderCountResponse getRetailerStats(Long retailerId);

    OrderCountResponse getCustomerOrderCount(Long customerId);

    long getActiveOrders(Long customerId);

    String getTotalRevenue();

    String getRevenueByMerchant(Long merchantId);

    String getRevenueByRetailer(Long retailerId);

    String getRevenueByCustomer(Long customerId);

    List<RecentOrderDto> getRecentOrders(Long userId);

    List<RecentOrderDto> getRecentOrdersForSeller(Long sellerId);

    OrderCountResponse getOrdersBetween(LocalDateTime start, LocalDateTime end);

    OrderCountResponse getMerchantOrdersBetween(Long merchantId, LocalDateTime start, LocalDateTime end);

    OrderCountResponse getRetailerOrdersBetween(Long retailerId, LocalDateTime start, LocalDateTime end);

    OrderCountResponse getCustomerOrdersBetween(Long customerId, LocalDateTime start, LocalDateTime end);

    List<OrderGraphDto> getOrderGraph(Long sourceId, Long userId);
}