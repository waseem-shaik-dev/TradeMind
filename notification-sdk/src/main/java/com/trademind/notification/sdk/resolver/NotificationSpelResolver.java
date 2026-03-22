package com.trademind.notification.sdk.resolver;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.*;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class NotificationSpelResolver {

    private static final ExpressionParser parser = new SpelExpressionParser();

    public static Object resolve(
            String expression,
            ProceedingJoinPoint joinPoint,
            Object result
    ) {

        if (expression == null || expression.isBlank()) {
            return null;
        }

        try {
            StandardEvaluationContext context = new StandardEvaluationContext();

            // method params
            Object[] args = joinPoint.getArgs();
            String[] paramNames =
                    ((MethodSignature) joinPoint.getSignature()).getParameterNames();

            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }

            // result
            context.setVariable("result", result);

            Expression exp = parser.parseExpression(expression);

            return exp.getValue(context);

        } catch (Exception ex) {
            return null;
        }
    }
}