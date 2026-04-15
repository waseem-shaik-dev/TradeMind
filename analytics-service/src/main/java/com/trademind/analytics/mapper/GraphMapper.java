package com.trademind.analytics.mapper;

import com.trademind.analytics.dto.common.GraphPointDto;
import com.trademind.analytics.client.billing.dto.RevenueGraphDto;
import com.trademind.analytics.client.order.dto.OrderGraphDto;

import java.util.List;

public class GraphMapper {

    public static List<GraphPointDto> mapRevenue(List<RevenueGraphDto> list) {
        return list.stream()
                .map(r -> GraphPointDto.builder()
                        .label(r.getLabel())
                        .value(r.getRevenue())
                        .build())
                .toList();
    }

    public static List<GraphPointDto> mapOrders(List<OrderGraphDto> list) {
        return list.stream()
                .map(o -> GraphPointDto.builder()
                        .label(o.getLabel())
                        .value(String.valueOf(o.getCount()))
                        .build())
                .toList();
    }
}