package com.trademind.order.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCountResponse {

    private Long  totalOrders;
    private Long  pendingOrders;
    private Long  completedOrders;
}