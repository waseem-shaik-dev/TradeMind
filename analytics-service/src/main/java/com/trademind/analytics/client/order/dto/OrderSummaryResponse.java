package com.trademind.analytics.client.order.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderSummaryResponse {

    private String orderNumber;
    private Long userId;
    private String amount;
    private String status;
    private String time;
}


//private String orderNumber;
//private Long userId;
//private String amount;
//private String status;
//private String createdAt;