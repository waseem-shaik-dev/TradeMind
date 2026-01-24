package com.trademind.product.dto;

public record CreateCategoryRequest(
        String name,
        Long parentCategoryId
) {}
