package com.trademind.audit.controller;

import com.trademind.audit.dto.AuditLogResponseDto;
import com.trademind.audit.dto.AuditLogSearchRequestDto;
import com.trademind.audit.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    /**
     * 🔍 Search audit logs with filters, pagination, sorting
     */
    @PostMapping("/search")
    public Page<AuditLogResponseDto> searchAuditLogs(
            @RequestBody AuditLogSearchRequestDto request
    ) {
        return auditLogService.search(request);
    }

    @GetMapping("/entity")
    public Page<AuditLogResponseDto> getByEntity(
            @RequestParam String entityType,
            @RequestParam String entityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        AuditLogSearchRequestDto request = AuditLogSearchRequestDto.builder()
                .entityType(Enum.valueOf(com.trademind.audit.enums.EntityType.class, entityType))
                .entityId(entityId)
                .page(page)
                .size(size)
                .build();

        return auditLogService.search(request);
    }
}