package com.trademind.analytics.aggregator.admin;

import com.trademind.analytics.client.audit.AuditClient;
import com.trademind.analytics.client.billing.BillingClient;
import com.trademind.analytics.client.order.OrderClient;
import com.trademind.analytics.client.order.dto.OrderCountResponse;
import com.trademind.analytics.client.user.UserClient;
import com.trademind.analytics.client.user.dto.UserCountResponse;
import com.trademind.analytics.dto.admin.AdminDashboardResponse;
import com.trademind.analytics.dto.common.*;
import com.trademind.analytics.helper.BillingAnalyticsHelper;
import com.trademind.analytics.helper.OrderAnalyticsHelper;
import com.trademind.analytics.mapper.AuditMapper;
import com.trademind.analytics.mapper.TopPerformerMapper;
import com.trademind.analytics.util.ChangeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Component
@RequiredArgsConstructor
public class AdminDashboardAggregator {

    private final UserClient userClient;
    private final OrderClient orderClient;
    private final BillingClient billingClient;
    private final Executor analyticsExecutor;
    private final AuditClient auditClient;
    private final BillingAnalyticsHelper billingHelper;
    private final OrderAnalyticsHelper orderHelper;

    public AdminDashboardResponse getDashboard() {

        CompletableFuture<?> usersFuture = CompletableFuture.supplyAsync(
                userClient::getUserCounts, analyticsExecutor
        );

        CompletableFuture<OrderCountResponse> ordersFuture = CompletableFuture.supplyAsync(
                orderClient::getTotalOrders, analyticsExecutor
        );

        CompletableFuture<Long> currentOrders =
                CompletableFuture.supplyAsync(() ->
                                orderHelper.getCurrentOrders(30),
                        analyticsExecutor
                );

        CompletableFuture<Long> previousOrders =
                CompletableFuture.supplyAsync(() ->
                                orderHelper.getPreviousOrders(30),
                        analyticsExecutor
                );

        CompletableFuture<BigDecimal> revenueFuture =
                CompletableFuture.supplyAsync(() ->
                                billingHelper.getCurrentRevenue(30, null, null),
                        analyticsExecutor
                );

        CompletableFuture<BigDecimal> prevRevenueFuture =
                CompletableFuture.supplyAsync(() ->
                                billingHelper.getPreviousRevenue(30, null, null),
                        analyticsExecutor
                );

        CompletableFuture<List<RecentActivityDto>> activityFuture =
                CompletableFuture.supplyAsync(() ->
                                auditClient.getRecentActivities(10)
                                        .stream()
                                        .map(AuditMapper::map)
                                        .toList(),
                        analyticsExecutor
                );

        CompletableFuture<List<TopPerformerDto>> topMerchantsFuture =
                CompletableFuture.supplyAsync(() ->
                                TopPerformerMapper.map(
                                        billingClient.getTopMerchants(5)
                                ),
                        analyticsExecutor
                );

        CompletableFuture<UserCountResponse> currentUsers =
                CompletableFuture.supplyAsync(() ->
                                userClient.getUserCountsByRange(
                                        LocalDateTime.now().minusDays(30),
                                        LocalDateTime.now()
                                ),
                        analyticsExecutor
                );

        CompletableFuture<UserCountResponse> previousUsers =
                CompletableFuture.supplyAsync(() ->
                                userClient.getUserCountsByRange(
                                        LocalDateTime.now().minusDays(60),
                                        LocalDateTime.now().minusDays(30)
                                ),
                        analyticsExecutor
                );



        CompletableFuture.allOf(usersFuture, ordersFuture, revenueFuture,prevRevenueFuture,activityFuture,topMerchantsFuture,currentUsers,previousUsers,
                currentOrders,
                previousOrders
        ).join();

        var users = (com.trademind.analytics.client.user.dto.UserCountResponse) usersFuture.join();
        OrderCountResponse totalOrders = ordersFuture.join();


        UserCountResponse curr = currentUsers.join();
        UserCountResponse prev = previousUsers.join();

        BigDecimal revenue = revenueFuture.join();
        BigDecimal prevRevenue = prevRevenueFuture.join();

        String change = ChangeUtil.calculateChange(revenue, prevRevenue);
        boolean positive = ChangeUtil.isPositive(revenue, prevRevenue);

        long currOrders = currentOrders.join();
        long prevOrders = previousOrders.join();

        List<RecentActivityDto> activities =
                activityFuture.exceptionally(ex -> List.of()).join();
        List<TopPerformerDto> topMerchants = topMerchantsFuture.exceptionally(ex->List.of()).join();

        List<MetricCardDto> metrics = List.of(
                MetricCardDto.builder()
                        .title("Total Merchants")
                        .value(String.valueOf(users.getTotalMerchants()))
                        .change(ChangeUtil.calculateChange(
                                BigDecimal.valueOf(curr.getTotalMerchants()),
                                BigDecimal.valueOf(prev.getTotalMerchants())
                        ))
                        .positive(ChangeUtil.isPositive(
                                BigDecimal.valueOf(curr.getTotalMerchants()),
                                BigDecimal.valueOf(prev.getTotalMerchants())
                        ))
                        .build(),

                MetricCardDto.builder()
                        .title("Total Retailers")
                        .value(String.valueOf(users.getTotalRetailers()))
                        .change(ChangeUtil.calculateChange(
                        BigDecimal.valueOf(curr.getTotalRetailers()),
                        BigDecimal.valueOf(prev.getTotalRetailers())
                ))
                .positive(ChangeUtil.isPositive(
                        BigDecimal.valueOf(curr.getTotalRetailers()),
                        BigDecimal.valueOf(prev.getTotalRetailers())
                ))
                        .build(),

                MetricCardDto.builder()
                        .title("Total Customers")
                        .value(String.valueOf(users.getTotalCustomers()))
                        .change(ChangeUtil.calculateChange(
                                BigDecimal.valueOf(curr.getTotalCustomers()),
                                BigDecimal.valueOf(prev.getTotalCustomers())
                        ))
                        .positive(ChangeUtil.isPositive(
                                BigDecimal.valueOf(curr.getTotalCustomers()),
                                BigDecimal.valueOf(prev.getTotalCustomers())
                        ))
                        .build(),

                MetricCardDto.builder()
                        .title("Total Revenue")
                        .value(revenue.toString())
                        .change(change)
                        .positive(positive)
                        .build(),


        MetricCardDto.builder()
                .title("Total Orders")
                .value(String.valueOf(totalOrders.getTotalOrders()))
                .change(ChangeUtil.calculateChange(
                        java.math.BigDecimal.valueOf(currOrders),
                        java.math.BigDecimal.valueOf(prevOrders)
                ))
                .positive(ChangeUtil.isPositive(
                        java.math.BigDecimal.valueOf(currOrders),
                        java.math.BigDecimal.valueOf(prevOrders)
                ))
                .build()
        );

        return AdminDashboardResponse.builder()
                .metrics(metrics)
                .recentActivities(activities) // fill later (notification/audit)
                .topMerchants(topMerchants)     // fill later
                .build();
    }
}
