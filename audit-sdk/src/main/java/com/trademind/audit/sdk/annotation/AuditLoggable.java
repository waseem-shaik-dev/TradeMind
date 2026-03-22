package com.trademind.audit.sdk.annotation;


import com.trademind.events.audit.enums.AuditAction;
import com.trademind.events.audit.enums.EntityType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditLoggable {

    AuditAction action();

    EntityType entityType();

    String entityIdExpression() default "";
    // SpEL expression like "#order.id"

    boolean captureBefore() default false;

    boolean captureAfter() default true;
}