package com.trademind.analytics.aggregator.retailer;

import com.trademind.analytics.client.billing.BillingClient;
import com.trademind.analytics.client.inventory.InventoryClient;
import com.trademind.analytics.client.order.OrderClient;
import com.trademind.analytics.client.product.ProductClient;
import com.trademind.analytics.dto.common.*;
import com.trademind.analytics.dto.retailer.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class RetailerDashboardAggregator {

    private final ProductClient productClient;
    private final OrderClient orderClient;
    private final BillingClient billingClient;
    private final InventoryClient inventoryClient;

    public RetailerDashboardResponse getDashboard(Long retailerId) {

        CompletableFuture<Long> products =
                CompletableFuture.supplyAsync(() ->
                        productClient.getProductsByRetailer(retailerId));

        CompletableFuture<Long> orders =
                CompletableFuture.supplyAsync(() ->
                        orderClient.getOrdersByRetailer(retailerId));

        CompletableFuture<String> todaySales =
                CompletableFuture.supplyAsync(() ->
                        billingClient.getTodaySales(retailerId));

        CompletableFuture<Integer> lowStock =
                CompletableFuture.supplyAsync(() ->
                        inventoryClient.getLowStockCount(retailerId));

        CompletableFuture.allOf(products, orders, todaySales, lowStock).join();

        List<MetricCardDto> metrics = List.of(
                MetricCardDto.builder().title("Products").value(String.valueOf(products.join())).build(),
                MetricCardDto.builder().title("Orders").value(String.valueOf(orders.join())).build(),
                MetricCardDto.builder().title("Today's Sales").value(todaySales.join()).build()
        );

        QuickStatsDto stats = QuickStatsDto.builder()
                .weeklySales("$12,450")
                .monthlySales("$48,200")
                .customers(256)
                .lowStockItems(lowStock.join())
                .build();

        return RetailerDashboardResponse.builder()
                .metrics(metrics)
                .recentSales(List.of())
                .quickStats(stats)
                .build();
    }
}