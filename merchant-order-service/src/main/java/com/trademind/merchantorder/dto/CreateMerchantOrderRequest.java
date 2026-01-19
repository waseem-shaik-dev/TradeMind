package com.trademind.merchantorder.dto;

import java.util.List;

public record CreateMerchantOrderRequest(
        Long retailerId,
        Long merchantId,
        List<OrderItemRequest> items
) {}

