package com.trademind.analytics.mapper;

import com.trademind.analytics.client.user.dto.UserGraphDto;
import com.trademind.analytics.dto.admin.UserGraphPointDto;

import java.util.List;

public class UserGraphMapper {

    public static List<UserGraphPointDto> map(List<UserGraphDto> list) {

        return list.stream()
                .map(u -> UserGraphPointDto.builder()
                        .label(u.getLabel())
                        .merchants(u.getMerchants())
                        .retailers(u.getRetailers())
                        .customers(u.getCustomers())
                        .build())
                .toList();
    }
}
