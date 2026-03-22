package com.trademind.notification.util;

import java.util.Map;

public class TemplateUtil {

    public static String replacePlaceholders(
            String template,
            Map<String, Object> data
    ) {

        if (template == null || data == null) {
            return template;
        }

        String result = template;

        for (Map.Entry<String, Object> entry : data.entrySet()) {

            String key = "{{" + entry.getKey() + "}}";
            String value = entry.getValue() != null
                    ? entry.getValue().toString()
                    : "";

            result = result.replace(key, value);
        }

        return result;
    }
}