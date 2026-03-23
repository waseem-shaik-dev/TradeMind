package com.trademind.analytics.aggregator.admin;

import com.trademind.analytics.client.billing.BillingClient;
import com.trademind.analytics.client.order.OrderClient;
import com.trademind.analytics.client.user.UserClient;
import com.trademind.analytics.dto.admin.AdminDashboardResponse;
import com.trademind.analytics.dto.common.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class AdminDashboardAggregator {

    private final UserClient userClient;
    private final OrderClient orderClient;
    private final BillingClient billingClient;

    public AdminDashboardResponse getDashboard() {

        CompletableFuture<?> usersFuture = CompletableFuture.supplyAsync(
                userClient::getUserCounts
        );

        CompletableFuture<Long> ordersFuture = CompletableFuture.supplyAsync(
                orderClient::getTotalOrders
        );

        CompletableFuture<String> revenueFuture = CompletableFuture.supplyAsync(
                billingClient::getTotalRevenue
        );

        CompletableFuture.allOf(usersFuture, ordersFuture, revenueFuture).join();

        var users = (com.trademind.analytics.client.user.dto.UserCountResponse) usersFuture.join();
        Long totalOrders = ordersFuture.join();
        String revenue = revenueFuture.join();

        List<MetricCardDto> metrics = List.of(
                MetricCardDto.builder()
                        .title("Total Merchants")
                        .value(String.valueOf(users.getTotalMerchants()))
                        .change("+12%")
                        .positive(true)
                        .build(),

                MetricCardDto.builder()
                        .title("Total Retailers")
                        .value(String.valueOf(users.getTotalRetailers()))
                        .change("+8%")
                        .positive(true)
                        .build(),

                MetricCardDto.builder()
                        .title("Total Customers")
                        .value(String.valueOf(users.getTotalCustomers()))
                        .change("+15%")
                        .positive(true)
                        .build(),

                MetricCardDto.builder()
                        .title("Total Revenue")
                        .value(revenue)
                        .change("+22%")
                        .positive(true)
                        .build()
        );

        return AdminDashboardResponse.builder()
                .metrics(metrics)
                .recentActivities(List.of()) // fill later (notification/audit)
                .topMerchants(List.of())     // fill later
                .build();
    }
}