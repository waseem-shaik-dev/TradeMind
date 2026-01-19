package com.trademind.audit.serviceImpl;

import com.trademind.audit.entity.AuditLog;
import com.trademind.audit.repository.AuditLogRepository;
import com.trademind.audit.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository repo;

    @Override
    public List<AuditLog> getAll() {
        return repo.findAll();
    }

    @Override
    public List<AuditLog> getByUser(Long userId) {
        return repo.findByPerformedBy(userId);
    }

    @Override
    public List<AuditLog> getByService(String serviceName) {
        return repo.findByServiceName(serviceName);
    }

    @Override
    public List<AuditLog> getByEntity(String entity, String entityId) {
        return repo.findByEntityNameAndEntityId(entity, entityId);
    }
}

