package com.trademind.auth.dto;

import com.trademind.audit.sdk.contract.AuditActorAware;

public record AuthResponse(
        Long userId,
        String userRole,
        String accessToken,
        String userEmail
) implements AuditActorAware {

    @Override
    public Long getAuditUserId() {
        return userId;
    }

    @Override
    public String getAuditUserRole() {
        return userRole;
    }
}
