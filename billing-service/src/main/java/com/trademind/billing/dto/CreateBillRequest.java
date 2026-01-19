package com.trademind.billing.dto;

import java.util.List;

public record CreateBillRequest(
        Long retailerId,
        Long customerId,
        List<BillItemRequest> items
) {}
