package com.trademind.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceRequest(
        BigDecimal price,
        LocalDateTime effectiveFrom
) {}
