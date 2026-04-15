package com.trademind.analytics.aggregator.customer;

import com.trademind.analytics.client.billing.BillingClient;
import com.trademind.analytics.client.order.OrderClient;
import com.trademind.analytics.client.order.dto.OrderCountResponse;
import com.trademind.analytics.client.order.dto.OrderSummaryResponse;
import com.trademind.analytics.dto.common.*;
import com.trademind.analytics.dto.customer.CustomerDashboardResponse;
import com.trademind.analytics.helper.BillingAnalyticsHelper;
import com.trademind.analytics.helper.OrderAnalyticsHelper;
import com.trademind.analytics.mapper.GraphMapper;
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
public class CustomerDashboardAggregator {

    private final OrderClient orderClient;
    private final BillingClient billingClient;
    private final Executor analyticsExecutor;
    private final BillingAnalyticsHelper billingHelper;
    private final OrderAnalyticsHelper orderHelper;

    public CustomerDashboardResponse getDashboard(Long customerId) {

        CompletableFuture<Long> currOrdersFuture =
                CompletableFuture.supplyAsync(() ->
                                orderHelper.getCustomerCurrent(customerId, 30),
                        analyticsExecutor
                );

        CompletableFuture<Long> prevOrdersFuture =
                CompletableFuture.supplyAsync(() ->
                                orderHelper.getCustomerPrevious(customerId, 30),
                        analyticsExecutor
                );

        CompletableFuture<OrderCountResponse> totalOrders =
                CompletableFuture.supplyAsync(() ->
                        orderClient.getOrdersByCustomer(customerId), analyticsExecutor
                );

        CompletableFuture<List<GraphPointDto>> spendingGraphFuture =
                CompletableFuture.supplyAsync(() ->
                                GraphMapper.mapRevenue(
                                        billingClient.getRevenueGraph(null, null, customerId)
                                ),
                        analyticsExecutor
                );

        CompletableFuture<List<GraphPointDto>> orderGraphFuture =
                CompletableFuture.supplyAsync(() ->
                                GraphMapper.mapOrders(
                                        orderClient.getOrderGraph(null, customerId)
                                ),
                        analyticsExecutor
                );

        CompletableFuture<Long> activeOrders =
                CompletableFuture.supplyAsync(() ->
                        orderClient.getActiveOrders(customerId), analyticsExecutor);

        CompletableFuture<BigDecimal> totalSpent =
                CompletableFuture.supplyAsync(() ->
                                billingHelper.getCurrentRevenue(30, "CUSTOMER", customerId),
                        analyticsExecutor
                );

        CompletableFuture<List<OrderSummaryResponse>> ordersFuture =
                CompletableFuture.supplyAsync(() ->
                        orderClient.getRecentOrders(customerId), analyticsExecutor);

        CompletableFuture.allOf(totalOrders, activeOrders, totalSpent,ordersFuture,
                currOrdersFuture,
                prevOrdersFuture,
                orderGraphFuture,
                spendingGraphFuture
                ).join();

        OrderCountResponse orderStats = totalOrders.join();

        long currOrders = currOrdersFuture.join();
        long prevOrders = prevOrdersFuture.join();

        List<GraphPointDto> ordersGraph = orderGraphFuture.join();

        List<GraphPointDto> spendingGraph = spendingGraphFuture.join();




        List<MetricCardDto> metrics = List.of(
                MetricCardDto.builder()
                        .title("Total Orders")
                        .value(String.valueOf(orderStats.getTotalOrders()))
                        .change(ChangeUtil.calculateChange(
                                java.math.BigDecimal.valueOf(currOrders),
                                java.math.BigDecimal.valueOf(prevOrders)
                        ))
                        .positive(ChangeUtil.isPositive(
                                java.math.BigDecimal.valueOf(currOrders),
                                java.math.BigDecimal.valueOf(prevOrders)
                        ))
                        .build(),

                MetricCardDto.builder().title("Active Orders").value(String.valueOf(activeOrders.join())).build(),
                MetricCardDto.builder().title("Total Spent").value(totalSpent.join().toString()).build()

        );

        List<OrderSummaryDto> recentOrders = OrderMapper.map(ordersFuture.join());

        CustomerDashboardResponse res = CustomerDashboardResponse.builder()
                .metrics(metrics)
                .recentOrders(recentOrders)
                .orderGraph(ordersGraph)
                .spendingGraph(spendingGraph)
                .build();


        System.out.println("\n\n\n response received in customer aggregator:   "+res+" \n\n\n");

        return res;
    }
}