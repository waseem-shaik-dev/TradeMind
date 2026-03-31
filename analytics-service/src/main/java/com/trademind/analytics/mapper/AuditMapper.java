package com.trademind.analytics.mapper;

import com.trademind.analytics.dto.common.RecentActivityDto;
import com.trademind.analytics.client.audit.dto.AuditLogResponseDto;

public class AuditMapper {

    public static RecentActivityDto map(AuditLogResponseDto a) {

        return RecentActivityDto.builder()
                .title(a.getAction().name())
                .subtitle(a.getEntityType() + " - " + a.getEntityId())
                .timeAgo(a.getTimestamp().toString())
                .type(a.getEntityType().name())
                .build();
    }
}