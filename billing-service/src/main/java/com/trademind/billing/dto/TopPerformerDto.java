package com.trademind.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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