package com.trademind.analytics.dto.common;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderSummaryDto {

    private String orderId;
    private String customerOrRetailer;
    private String amount;
    private String status; // pending, shipped, delivered
    private String time;
}

