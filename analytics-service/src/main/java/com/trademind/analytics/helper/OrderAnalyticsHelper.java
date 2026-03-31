package com.trademind.analytics.helper;

import com.trademind.analytics.client.order.OrderClient;
import com.trademind.analytics.client.order.dto.OrderCountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OrderAnalyticsHelper {

    private final OrderClient orderClient;

    // ================= COMMON =================

    private LocalDateTime now() {
        return LocalDateTime.now();
    }

    private LocalDateTime start(int days) {
        return now().minusDays(days);
    }

    private LocalDateTime previousStart(int days) {
        return now().minusDays(days * 2);
    }

    private LocalDateTime previousEnd(int days) {
        return now().minusDays(days);
    }

    // ================= ADMIN =================

    public long getCurrentOrders(int days) {
        OrderCountResponse res = orderClient.getOrdersBetween(
                start(days),
                now()
        );
        return res.getTotalOrders();
    }

    public long getPreviousOrders(int days) {
        OrderCountResponse res = orderClient.getOrdersBetween(
                previousStart(days),
                previousEnd(days)
        );
        return res.getTotalOrders();
    }

    // ================= MERCHANT =================

    public long getMerchantCurrent(Long merchantId, int days) {
        OrderCountResponse res = orderClient.getMerchantOrdersBetween(
                merchantId,
                start(days),
                now()
        );
        return res.getTotalOrders();
    }

    public long getMerchantPrevious(Long merchantId, int days) {
        OrderCountResponse res = orderClient.getMerchantOrdersBetween(
                merchantId,
                previousStart(days),
                previousEnd(days)
        );
        return res.getTotalOrders();
    }

    // ================= RETAILER =================

    public long getRetailerCurrent(Long retailerId, int days) {
        OrderCountResponse res = orderClient.getRetailerOrdersBetween(
                retailerId,
                start(days),
                now()
        );
        return res.getTotalOrders();
    }

    public long getRetailerPrevious(Long retailerId, int days) {
        OrderCountResponse res = orderClient.getRetailerOrdersBetween(
                retailerId,
                previousStart(days),
                previousEnd(days)
        );
        return res.getTotalOrders();
    }

    // ================= CUSTOMER =================

    public long getCustomerCurrent(Long customerId, int days) {
        OrderCountResponse res = orderClient.getCustomerOrdersBetween(
                customerId,
                start(days),
                now()
        );
        return res.getTotalOrders();
    }

    public long getCustomerPrevious(Long customerId, int days) {
        OrderCountResponse res = orderClient.getCustomerOrdersBetween(
                customerId,
                previousStart(days),
                previousEnd(days)
        );
        return res.getTotalOrders();
    }
}