package com.trademind.analytics.dto.common;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MetricCardDto {

    private String title;          // e.g. "Total Revenue"
    private String value;          // "$1.2M"
    private String change;         // "+22%"
    private boolean positive;      // true = green, false = red
    private String icon;           // optional (UI icon mapping)
}