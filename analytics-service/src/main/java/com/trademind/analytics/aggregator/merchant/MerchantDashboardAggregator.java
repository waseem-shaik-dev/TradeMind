package com.trademind.analytics.aggregator.merchant;

import com.trademind.analytics.client.billing.BillingClient;
import com.trademind.analytics.client.inventory.InventoryClient;
import com.trademind.analytics.client.order.OrderClient;
import com.trademind.analytics.client.product.ProductClient;
import com.trademind.analytics.dto.common.*;
import com.trademind.analytics.dto.merchant.MerchantDashboardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class MerchantDashboardAggregator {

    private final ProductClient productClient;
    private final OrderClient orderClient;
    private final BillingClient billingClient;
    private final InventoryClient inventoryClient;

    public MerchantDashboardResponse getDashboard(Long merchantId) {

        CompletableFuture<Long> productsFuture =
                CompletableFuture.supplyAsync(() ->
                        productClient.getProductsByMerchant(merchantId)
                );

        CompletableFuture<Long> ordersFuture =
                CompletableFuture.supplyAsync(() ->
                        orderClient.getOrdersByMerchant(merchantId)
                );

        CompletableFuture<String> revenueFuture =
                CompletableFuture.supplyAsync(() ->
                        billingClient.getRevenueByMerchant(merchantId)
                );

        CompletableFuture<List<com.trademind.analytics.client.inventory.dto.LowStockResponse>> stockFuture =
                CompletableFuture.supplyAsync(() ->
                        inventoryClient.getLowStockProducts(merchantId)
                );

        CompletableFuture.allOf(productsFuture, ordersFuture, revenueFuture, stockFuture).join();

        List<MetricCardDto> metrics = List.of(
                MetricCardDto.builder().title("Total Products").value(String.valueOf(productsFuture.join())).build(),
                MetricCardDto.builder().title("Orders").value(String.valueOf(ordersFuture.join())).build(),
                MetricCardDto.builder().title("Revenue").value(revenueFuture.join()).build()
        );

        List<LowStockDto> lowStock = stockFuture.join().stream()
                .map(s -> LowStockDto.builder()
                        .productName(s.getProductName())
                        .sku(s.getSku())
                        .remaining(s.getRemaining())
                        .minRequired(s.getMinRequired())
                        .build())
                .toList();

        return MerchantDashboardResponse.builder()
                .metrics(metrics)
                .recentOrders(List.of()) // next
                .lowStockAlerts(lowStock)
                .build();
    }
}