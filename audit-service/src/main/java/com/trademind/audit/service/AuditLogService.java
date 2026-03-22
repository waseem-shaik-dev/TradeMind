package com.trademind.audit.service;

import com.trademind.audit.dto.AuditLogResponseDto;
import com.trademind.audit.dto.AuditLogSearchRequestDto;
import com.trademind.events.audit.AuditEvent;
import org.springframework.data.domain.Page;

public interface AuditLogService {

    /**
     * Process incoming Kafka event
     */
    void processAuditEvent(AuditEvent event);

    /**
     * Search audit logs with filters
     */
    Page<AuditLogResponseDto> search(AuditLogSearchRequestDto request);
}