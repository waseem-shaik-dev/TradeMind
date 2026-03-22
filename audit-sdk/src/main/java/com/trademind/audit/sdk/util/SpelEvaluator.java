package com.trademind.audit.sdk.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.expression.*;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

public class SpelEvaluator {

    private static final ExpressionParser parser = new SpelExpressionParser();

    public static Object evaluate(
            String expression,
            ProceedingJoinPoint joinPoint,
            Object result
    ) {

        if (expression == null || expression.isEmpty()) {
            return null;
        }

        StandardEvaluationContext context = new StandardEvaluationContext();

        // Method parameters
        Object[] args = joinPoint.getArgs();
        String[] paramNames =
                ((org.aspectj.lang.reflect.MethodSignature)
                        joinPoint.getSignature()).getParameterNames();

        for (int i = 0; i < paramNames.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }

        // Result object
        context.setVariable("result", result);

        Expression exp = parser.parseExpression(expression);
        return exp.getValue(context);
    }
}