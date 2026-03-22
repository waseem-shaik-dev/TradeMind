package com.trademind.notification.template;

import com.trademind.events.notification.enums.NotificationType;

import java.util.Map;

public interface TemplateService {

    String render(NotificationType type, Map<String, Object> data);
}