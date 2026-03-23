package com.trademind.analytics.aggregator.customer;

import com.trademind.analytics.client.billing.BillingClient;
import com.trademind.analytics.client.order.OrderClient;
import com.trademind.analytics.dto.common.*;
import com.trademind.analytics.dto.customer.CustomerDashboardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class CustomerDashboardAggregator {

    private final OrderClient orderClient;
    private final BillingClient billingClient;

    public CustomerDashboardResponse getDashboard(Long customerId) {

        CompletableFuture<Long> totalOrders =
                CompletableFuture.supplyAsync(() ->
                        orderClient.getTotalOrders());

        CompletableFuture<Long> activeOrders =
                CompletableFuture.supplyAsync(() ->
                        orderClient.getActiveOrders(customerId));

        CompletableFuture<String> totalSpent =
                CompletableFuture.supplyAsync(() ->
                        billingClient.getRevenueByRetailer(customerId));

        CompletableFuture.allOf(totalOrders, activeOrders, totalSpent).join();

        List<MetricCardDto> metrics = List.of(
                MetricCardDto.builder().title("Total Orders").value(String.valueOf(totalOrders.join())).build(),
                MetricCardDto.builder().title("Active Orders").value(String.valueOf(activeOrders.join())).build(),
                MetricCardDto.builder().title("Total Spent").value(totalSpent.join()).build()
        );

        return CustomerDashboardResponse.builder()
                .metrics(metrics)
                .recentOrders(List.of())
                .build();
    }
}