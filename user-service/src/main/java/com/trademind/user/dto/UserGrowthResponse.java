package com.trademind.user.dto;

import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserGrowthResponse {

    private Map<String, Long> dailyGrowth; // date → count
}