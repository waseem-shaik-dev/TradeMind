package com.trademind.billing.service;

import com.trademind.billing.dto.BillResponse;
import com.trademind.billing.dto.CreateBillRequest;

public interface BillingService {
    BillResponse generateBill(CreateBillRequest request);
}
