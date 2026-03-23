package com.trademind.analytics.dto.common;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecentActivityDto {

    private String title;       // "New merchant registered"
    private String subtitle;    // "Tech Solutions Ltd"
    private String timeAgo;     // "2 min ago"
    private String type;        // ORDER / USER / INVENTORY
}