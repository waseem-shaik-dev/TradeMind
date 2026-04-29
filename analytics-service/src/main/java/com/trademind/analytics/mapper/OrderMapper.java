package com.trademind.analytics.mapper;

import com.trademind.analytics.dto.common.OrderSummaryDto;
import com.trademind.analytics.client.order.dto.OrderSummaryResponse;

import java.util.List;

public class OrderMapper {

    public static List<OrderSummaryDto> map(List<OrderSummaryResponse> list) {
        return list.stream()
                .map(o -> OrderSummaryDto.builder()
                        .orderId(o.getOrderNumber())
                        .customerOrRetailer("User "+o.getUserId())
                        .amount(o.getAmount())
                        .status(o.getStatus())
                        .time(o.getTime())
                        .build())
                .toList();
    }
}