package com.trademind.analytics.aggregator.retailer;

import com.trademind.analytics.client.billing.BillingClient;
import com.trademind.analytics.client.inventory.InventoryClient;
import com.trademind.analytics.client.order.OrderClient;
import com.trademind.analytics.client.order.dto.OrderCountResponse;
import com.trademind.analytics.client.order.dto.OrderSummaryResponse;
import com.trademind.analytics.client.product.ProductClient;
import com.trademind.analytics.dto.common.*;
import com.trademind.analytics.dto.retailer.*;
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
public class RetailerDashboardAggregator {

    private final ProductClient productClient;
    private final OrderClient orderClient;
    private final BillingClient billingClient;
    private final InventoryClient inventoryClient;
    private final Executor analyticsExecutor;
    private final BillingAnalyticsHelper billingHelper;
    private final OrderAnalyticsHelper orderHelper;


    public RetailerDashboardResponse getDashboard(Long retailerId) {

        CompletableFuture<Long> currOrdersFuture =
                CompletableFuture.supplyAsync(() ->
                                orderHelper.getRetailerCurrent(retailerId, 30),
                        analyticsExecutor
                );

        CompletableFuture<Long> prevOrdersFuture =
                CompletableFuture.supplyAsync(() ->
                                orderHelper.getRetailerPrevious(retailerId, 30),
                        analyticsExecutor
                );

        CompletableFuture<Long> products =
                CompletableFuture.supplyAsync(() ->
                                inventoryClient.getProductCount(retailerId, "RETAILER"),
                        analyticsExecutor
                );

        CompletableFuture<OrderCountResponse> ordersFuture =
                CompletableFuture.supplyAsync(() ->
                        orderClient.getOrdersByRetailer(retailerId), analyticsExecutor);

        CompletableFuture<BigDecimal> todaySales =
                CompletableFuture.supplyAsync(() ->
                        billingHelper.getCurrentRevenue(1, "RETAILER", retailerId), analyticsExecutor);

        CompletableFuture<Integer> lowStock =
                CompletableFuture.supplyAsync(() ->
                        inventoryClient.getLowStockCount(retailerId), analyticsExecutor);

        CompletableFuture<List<OrderSummaryResponse>> salesFuture =
                CompletableFuture.supplyAsync(() ->
                        orderClient.getRecentOrders(retailerId), analyticsExecutor);

        CompletableFuture<BigDecimal> weekly =
                CompletableFuture.supplyAsync(() ->
                                billingHelper.getCurrentRevenue(7, "RETAILER", retailerId),
                        analyticsExecutor
                );

        CompletableFuture<BigDecimal> monthly =
                CompletableFuture.supplyAsync(() ->
                                billingHelper.getCurrentRevenue(30, "RETAILER", retailerId),
                        analyticsExecutor
                );

        CompletableFuture.allOf(products, ordersFuture, todaySales, lowStock,salesFuture,monthly,weekly,
                currOrdersFuture,
                prevOrdersFuture
                ).join();

        OrderCountResponse orders = ordersFuture.join();

        long curOrders = currOrdersFuture.join();
        long prevOrders = prevOrdersFuture.join();

        List<MetricCardDto> metrics = List.of(
                MetricCardDto.builder().title("Products").value(String.valueOf(products.join())).build(),


        MetricCardDto.builder()
                .title("Orders")
                .value(String.valueOf(orders.getTotalOrders()))
                .change(ChangeUtil.calculateChange(
                        java.math.BigDecimal.valueOf(curOrders),
                        java.math.BigDecimal.valueOf(prevOrders)
                ))
                .positive(ChangeUtil.isPositive(
                        java.math.BigDecimal.valueOf(curOrders),
                        java.math.BigDecimal.valueOf(prevOrders)
                ))
                .build(),
                MetricCardDto.builder().title("Today's Sales").value(todaySales.join().toString()).build()
        );

        QuickStatsDto stats = QuickStatsDto.builder()
                .weeklySales(weekly.join().toString())
                .monthlySales(monthly.join().toString())
                .customers(26)
                .lowStockItems(lowStock.join())
                .build();

        List<OrderSummaryDto> recentSales = OrderMapper.map(salesFuture.join());

        return RetailerDashboardResponse.builder()
                .metrics(metrics)
                .recentSales(recentSales)
                .quickStats(stats)
                .build();
    }

}