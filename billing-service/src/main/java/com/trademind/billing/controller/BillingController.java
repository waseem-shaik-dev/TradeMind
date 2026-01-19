package com.trademind.billing.controller;

import com.trademind.billing.dto.BillResponse;
import com.trademind.billing.dto.CreateBillRequest;
import com.trademind.billing.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @PostMapping("/generate")
    public BillResponse generateBill(@RequestBody CreateBillRequest request) {
        return billingService.generateBill(request);
    }
}
