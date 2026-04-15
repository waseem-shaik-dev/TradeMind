package com.trademind.analytics.dto.common;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GraphPointDto {

    private String label;
    private String value;
}