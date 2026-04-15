package com.trademind.user.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserGraphDto {

    private String label;
    private long merchants;
    private long retailers;
    private long customers;
}