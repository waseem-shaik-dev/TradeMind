package com.trademind.analytics.dto.common;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopPerformerDto {

    private int rank;
    private String name;
    private String subtitle;   // "245 orders"
    private String value;      // "45,200"
}