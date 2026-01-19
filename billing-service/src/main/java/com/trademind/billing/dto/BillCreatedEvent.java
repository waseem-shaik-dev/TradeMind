package com.trademind.billing.dto;

import java.math.BigDecimal;

public record BillCreatedEvent(
        Long billId,
        BigDecimal amount,
        String customerEmail
) {}
