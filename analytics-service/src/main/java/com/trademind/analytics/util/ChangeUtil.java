package com.trademind.analytics.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ChangeUtil {

    public static String calculateChange(BigDecimal current, BigDecimal previous) {

        if (previous == null || previous.compareTo(BigDecimal.ZERO) == 0) {
            return "0";
        }

        BigDecimal change = current.subtract(previous)
                .divide(previous, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        return change.setScale(2, RoundingMode.HALF_UP) + "";
    }

    public static boolean isPositive(BigDecimal current, BigDecimal previous) {
        return current.compareTo(previous) >= 0;
    }
}