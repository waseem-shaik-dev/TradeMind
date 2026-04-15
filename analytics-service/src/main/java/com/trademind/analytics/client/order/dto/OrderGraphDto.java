package com.trademind.analytics.client.order.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderGraphDto {

    private String label;
    private long count;
}