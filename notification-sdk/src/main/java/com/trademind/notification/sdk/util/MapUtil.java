package com.trademind.notification.sdk.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {

    @SuppressWarnings("unchecked")
    public static Map<String, Object> safeCast(Object obj) {

        if (obj == null) {
            return null;
        }

        if (obj instanceof Map<?, ?> map) {
            Map<String, Object> result = new HashMap<>();

            for (Map.Entry<?, ?> entry : map.entrySet()) {
                result.put(
                        String.valueOf(entry.getKey()),
                        entry.getValue()
                );
            }

            return result;
        }

        return null;
    }
}