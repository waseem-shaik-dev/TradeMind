package com.trademind.audit.repository;

import com.trademind.audit.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository
        extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByServiceName(String serviceName);

    List<AuditLog> findByPerformedBy(Long performedBy);

    List<AuditLog> findByEntityNameAndEntityId(
            String entityName, String entityId
    );
}
