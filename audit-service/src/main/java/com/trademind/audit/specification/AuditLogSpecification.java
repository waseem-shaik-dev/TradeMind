package com.trademind.audit.specification;

import com.trademind.audit.entity.AuditLog;
import com.trademind.audit.dto.AuditLogSearchRequestDto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AuditLogSpecification {

    public static Specification<AuditLog> build(AuditLogSearchRequestDto req) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (req.getUserId() != null) {
                predicates.add(cb.equal(root.get("userId"), req.getUserId()));
            }

            if (req.getAction() != null) {
                predicates.add(cb.equal(root.get("action"), req.getAction()));
            }

            if (req.getEntityType() != null) {
                predicates.add(cb.equal(root.get("entityType"), req.getEntityType()));
            }

            if (req.getEntityId() != null) {
                predicates.add(cb.equal(root.get("entityId"), req.getEntityId()));
            }

            if (req.getServiceName() != null) {
                predicates.add(cb.equal(root.get("serviceName"), req.getServiceName()));
            }

            if (req.getStartTime() != null && req.getEndTime() != null) {
                predicates.add(
                        cb.between(
                                root.get("timestamp"),
                                req.getStartTime(),
                                req.getEndTime()
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}