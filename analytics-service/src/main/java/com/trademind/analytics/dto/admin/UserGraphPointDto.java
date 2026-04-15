package com.trademind.analytics.dto.admin;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserGraphPointDto {

    private String label;
    private long merchants;
    private long retailers;
    private long customers;
}