package com.trademind.audit.sdk.resolver;

public interface BeforeStateResolver {

    Object resolve(String entityId, String entityType);
}