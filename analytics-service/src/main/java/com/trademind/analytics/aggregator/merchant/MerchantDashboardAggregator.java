package com.trademind.analytics.aggregator.merchant;

import com.trademind.analytics.client.billing.BillingClient;
import com.trademind.analytics.client.inventory.InventoryClient;
import com.trademind.analytics.client.order.OrderClient;
import com.trademind.analytics.client.order.dto.OrderCountResponse;
import com.trademind.analytics.client.order.dto.OrderSummaryResponse;
import com.trademind.analytics.client.product.ProductClient;
import com.trademind.analytics.dto.common.*;
import com.trademind.analytics.dto.merchant.MerchantDashboardResponse;
import com.trademind.analytics.helper.BillingAnalyticsHelper;
import com.trademind.analytics.helper.OrderAnalyticsHelper;
import com.trademind.analytics.mapper.OrderMapper;
import com.trademind.analytics.util.ChangeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Component
@RequiredArgsConstructor
public class MerchantDashboardAggregator {

    private final ProductClient productClient;
    private final OrderClient orderClient;
    private final BillingClient billingClient;
    private final InventoryClient inventoryClient;
    private final Executor analyticsExecutor;
    private final BillingAnalyticsHelper billingHelper;
    private final OrderAnalyticsHelper orderHelper;


    public MerchantDashboardResponse getDashboard(Long merchantId) {

        CompletableFuture<Long> productsFuture =
                CompletableFuture.supplyAsync(() ->
                                inventoryClient.getProductCount(merchantId, "MERCHANT"),
                        analyticsExecutor
                );

        CompletableFuture<OrderCountResponse> ordersFuture =
                CompletableFuture.supplyAsync(() ->
                        orderClient.getOrdersByMerchant(merchantId), analyticsExecutor
                );

        CompletableFuture<Long> currOrdersFuture =
                CompletableFuture.supplyAsync(() ->
                                orderHelper.getMerchantCurrent(merchantId, 30),
                        analyticsExecutor
                );

        CompletableFuture<Long> prevOrdersFuture =
                CompletableFuture.supplyAsync(() ->
                                orderHelper.getMerchantPrevious(merchantId, 30),
                        analyticsExecutor
                );

        CompletableFuture<BigDecimal> revenueFuture =
                CompletableFuture.supplyAsync(() ->
                                billingHelper.getCurrentRevenue(30, "MERCHANT", merchantId),
                        analyticsExecutor
                );

        CompletableFuture<BigDecimal> prevRevenueFuture =
                CompletableFuture.supplyAsync(() ->
                                billingHelper.getPreviousRevenue(30, "MERCHANT", merchantId),
                        analyticsExecutor
                );

        CompletableFuture<List<com.trademind.analytics.client.inventory.dto.LowStockResponse>> stockFuture =
                CompletableFuture.supplyAsync(() ->
                        inventoryClient.getLowStockProducts(merchantId), analyticsExecutor
                );

        CompletableFuture<List<OrderSummaryResponse>> recentOrdersFuture =
                CompletableFuture.supplyAsync(() ->
                        orderClient.getRecentOrders(merchantId), analyticsExecutor);

        CompletableFuture.allOf(productsFuture, ordersFuture, revenueFuture,prevRevenueFuture, stockFuture,recentOrdersFuture,
                prevOrdersFuture,
                currOrdersFuture
                ).join();

        OrderCountResponse orders = ordersFuture.join();

        BigDecimal revenue = revenueFuture.join();
        BigDecimal prev = prevRevenueFuture.join();

        long currOrders = currOrdersFuture.join();
        long prevOrders = prevOrdersFuture.join();


        List<MetricCardDto> metrics = List.of(
                MetricCardDto.builder().title("Total Products").value(String.valueOf(productsFuture.join())).build(),


        MetricCardDto.builder()
                .title("Orders")
                .value(String.valueOf(orders.getTotalOrders()))
                .change(ChangeUtil.calculateChange(
                        java.math.BigDecimal.valueOf(currOrders),
                        java.math.BigDecimal.valueOf(prevOrders)
                ))
                .positive(ChangeUtil.isPositive(
                        java.math.BigDecimal.valueOf(currOrders),
                        java.math.BigDecimal.valueOf(prevOrders)
                ))
                .build(),

                MetricCardDto.builder()
                        .title("Revenue")
                        .value(revenue.toString())
                        .change(ChangeUtil.calculateChange(revenue, prev))
                        .positive(ChangeUtil.isPositive(revenue, prev))
                        .build()
        );

        List<LowStockDto> lowStock = stockFuture.join().stream()
                .map(s -> LowStockDto.builder()
                        .productName(s.getProductName())
                        .sku(s.getSku())
                        .remaining(s.getRemaining())
                        .minRequired(s.getMinRequired())
                        .build())
                .toList();

        List<OrderSummaryDto> recentOrders =
                OrderMapper.map(recentOrdersFuture.join());

        return MerchantDashboardResponse.builder()
                .metrics(metrics)
                .recentOrders(recentOrders) // next
                .lowStockAlerts(lowStock)
                .build();
    }
}