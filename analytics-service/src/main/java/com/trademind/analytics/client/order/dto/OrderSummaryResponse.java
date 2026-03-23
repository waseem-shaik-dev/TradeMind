package com.trademind.analytics.client.order.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderSummaryResponse {

    private String orderId;
    private String name;
    private String amount;
    private String status;
    private String time;
}