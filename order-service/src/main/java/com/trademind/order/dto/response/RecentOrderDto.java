package com.trademind.order.dto.response;

import com.trademind.order.enums.OrderStatus;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecentOrderDto {

    private String orderNumber;
    private Long userId;
    private String amount;
    private String status;
    private String createdAt;
}