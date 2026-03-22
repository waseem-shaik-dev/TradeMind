package com.trademind.audit.repository;

import com.trademind.audit.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface AuditLogRepository extends
        JpaRepository<AuditLog, UUID>,
        JpaSpecificationExecutor<AuditLog> {
}