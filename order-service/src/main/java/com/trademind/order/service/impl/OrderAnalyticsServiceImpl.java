package com.trademind.order.service.impl;

import com.trademind.order.dto.response.OrderCountResponse;
import com.trademind.order.dto.response.OrderGraphDto;
import com.trademind.order.dto.response.RecentOrderDto;
import com.trademind.order.repository.OrderRepository;
import com.trademind.order.service.OrderAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderAnalyticsServiceImpl implements OrderAnalyticsService {

    private final OrderRepository orderRepository;

    @Override
    public OrderCountResponse getGlobalStats() {
        return orderRepository.getGlobalOrderStats();
    }

    @Override
    public OrderCountResponse getMerchantStats(Long merchantId) {
        return orderRepository.getMerchantOrderStats(merchantId);
    }

    @Override
    public OrderCountResponse getRetailerStats(Long retailerId) {
        return orderRepository.getRetailerOrderStats(retailerId);
    }

    @Override
    public OrderCountResponse getCustomerOrderCount(Long customerId) {
        return orderRepository.getCustomerOrderStats(customerId);
    }

    @Override
    public long getActiveOrders(Long customerId) {
        return orderRepository.getActiveOrders(customerId);
    }

    @Override
    public String getTotalRevenue() {
        return orderRepository.getTotalRevenue().toString();
    }

    @Override
    public String getRevenueByMerchant(Long merchantId) {
        return orderRepository.getRevenueByMerchant(merchantId).toString();
    }

    @Override
    public String getRevenueByRetailer(Long retailerId) {
        return orderRepository.getRevenueByRetailer(retailerId).toString();
    }

    @Override
    public String getRevenueByCustomer(Long customerId) {
        return orderRepository.getRevenueByCustomer(customerId).toString();
    }

    @Override
    public List<RecentOrderDto> getRecentOrders(Long userId) {
        return orderRepository.getRecentOrders(userId,
                org.springframework.data.domain.PageRequest.of(0, 10));
    }

    @Override
    public List<RecentOrderDto> getRecentOrdersForSeller(Long sellerId) {
        return orderRepository.getRecentOrdersForSeller(sellerId,
                org.springframework.data.domain.PageRequest.of(0, 10));
    }

    @Override
    public OrderCountResponse getOrdersBetween(LocalDateTime start, LocalDateTime end) {
        return orderRepository.getOrderStatsBetween(start, end);
    }

    @Override
    public OrderCountResponse getMerchantOrdersBetween(Long id, LocalDateTime start, LocalDateTime end) {
        return orderRepository.getMerchantOrdersBetween(id, start, end);
    }

    @Override
    public OrderCountResponse getRetailerOrdersBetween(Long retailerId, LocalDateTime start, LocalDateTime end) {
        return orderRepository.getRetailerOrdersBetween(retailerId, start, end);

    }

    @Override
    public OrderCountResponse getCustomerOrdersBetween(Long customerId, LocalDateTime start, LocalDateTime end) {
        return orderRepository.getCustomerOrdersBetween(customerId, start, end);

    }

    public List<OrderGraphDto> getOrderGraph(Long sourceId, Long userId) {

        LocalDateTime start = LocalDateTime.now().minusDays(7);

        return orderRepository.getOrderGraph(start, sourceId, userId)
                .stream()
                .map(r -> new OrderGraphDto(
                        r[0].toString(),
                        (Long) r[1]
                ))
                .toList();
    }
}
