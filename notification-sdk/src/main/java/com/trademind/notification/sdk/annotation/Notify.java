package com.trademind.notification.sdk.annotation;

import com.trademind.events.notification.enums.NotificationType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Notify {

    // 🔹 Type of notification
    NotificationType type();

    // 🔹 Recipient (SpEL supported)
    String recipientExpression();
    // Example: "#result.email" or "#user.email"

    // 🔹 Template override (optional)
    String template() default "";

    // 🔹 Subject override (optional)
    String subject() default "";

    // 🔹 Data for template (SpEL expression)
    String dataExpression() default "";
    // Example: "{'orderId': #result.id, 'amount': #result.total}"

    // 🔹 Enable/disable notification dynamically
    boolean enabled() default true;
}