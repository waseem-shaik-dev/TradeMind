package com.trademind.audit.service;

import com.trademind.audit.entity.AuditLog;

import java.util.List;

public interface AuditLogService {

    List<AuditLog> getAll();

    List<AuditLog> getByUser(Long userId);

    List<AuditLog> getByService(String serviceName);

    List<AuditLog> getByEntity(String entity, String entityId);
}

