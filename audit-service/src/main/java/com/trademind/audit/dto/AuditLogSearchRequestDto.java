package com.trademind.audit.dto;

import com.trademind.events.audit.enums.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogSearchRequestDto {

    private String userId;

    private AuditAction action;

    private EntityType entityType;
    private String entityId;

    private String serviceName;

    private Instant startTime;
    private Instant endTime;

    // Pagination
    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 10;

    // Sorting
    @Builder.Default
    private String sortBy = "timestamp";

    @Builder.Default
    private String sortDirection = "DESC"; // ASC / DESC
}