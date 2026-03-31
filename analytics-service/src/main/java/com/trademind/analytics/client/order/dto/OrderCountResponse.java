package com.trademind.analytics.client.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCountResponse {

    private long totalOrders;
    private long pendingOrders;
    private long completedOrders;
}