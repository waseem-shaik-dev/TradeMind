package com.trademind.product.dto;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data
) {}
