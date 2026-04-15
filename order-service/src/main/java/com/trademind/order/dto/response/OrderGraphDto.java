package com.trademind.order.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderGraphDto {

    private String label;
    private long count;
}