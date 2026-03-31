package com.trademind.notification.template;

import com.trademind.events.notification.enums.NotificationType;
import com.trademind.notification.util.TemplateUtil;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SimpleTemplateService implements TemplateService {

    @Override
    public String render(NotificationType type, Map<String, Object> data) {

        String template = getTemplate(type);

        return TemplateUtil.replacePlaceholders(template, data);
    }

    private String getTemplate(NotificationType type) {

        return switch (type) {

            case USER_REGISTERED ->
                    "Hello {{name}}, welcome to TradeMind 🎉";

            case USER_LOGIN ->
                    "Hello {{name}}, you just logged in.";

            case ORDER_CREATED ->
                    "Your order with id: {{orderId}} has been placed successfully. Amount: ₹{{amount}}";

            case ORDER_CANCELLED ->
                    "Your order {{orderId}} has been cancelled.";

            case PAYMENT_SUCCESS ->
                    "Payment of ₹{{amount}} was successful.";

            case PAYMENT_FAILED ->
                    "Payment failed. Please try again.";

            default ->
                    "You have a new notification.";
        };
    }
}