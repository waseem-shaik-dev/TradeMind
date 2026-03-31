package com.trademind.billing.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RevenueTrendDto {

    private String date;
    private String revenue;
}