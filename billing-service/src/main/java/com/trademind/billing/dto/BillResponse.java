package com.trademind.billing.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record BillResponse(
        Long billId,
        String billNumber,
        BigDecimal totalAmount,
        String hash
) {}

