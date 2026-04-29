package com.trademind.audit.sdk.contract;

public interface AuditActorAware {

    Long getAuditUserId();
    String getAuditUserRole();
}