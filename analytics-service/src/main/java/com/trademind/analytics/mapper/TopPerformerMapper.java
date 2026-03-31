package com.trademind.analytics.mapper;

import com.trademind.analytics.client.billing.dto.TopMerchantRawDto;
import com.trademind.analytics.dto.common.TopPerformerDto;


import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TopPerformerMapper {

    public static List<TopPerformerDto> map(List<TopMerchantRawDto> list) {

        AtomicInteger rank = new AtomicInteger(1);

        return list.stream()
                .map(m -> TopPerformerDto.builder()
                        .rank(rank.getAndIncrement())
                        .name("Merchant " + m.getMerchantId()) // replace with user service later
                        .subtitle(m.getTotalOrders() + " orders")
                        .value(m.getRevenue())
                        .build())
                .toList();
    }
}