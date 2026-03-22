package com.trademind.audit.sdk.resolver;

import org.springframework.stereotype.Component;

@Component
public class DefaultBeforeStateResolver implements BeforeStateResolver {

    @Override
    public Object resolve(String entityId, String entityType) {
        // 🔥 Later connect to DB / service
        return null;
    }
}