package com.trademind.billing.controller;

import com.trademind.billing.dto.InvoiceResponseDto;
import com.trademind.billing.dto.InvoiceSummaryDto;
import com.trademind.billing.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billing/customer")
@RequiredArgsConstructor
public class CustomerBillingController {

    private final BillingService billingService;

    @GetMapping
    public Page<InvoiceSummaryDto> getMyInvoices(
            @RequestHeader("X-User-Id") Long userId,
            Pageable pageable
    ) {
        return billingService.getMyInvoices(userId, pageable);
    }

    @GetMapping("/{invoiceId}")
    public InvoiceResponseDto getInvoiceDetail(
            @PathVariable Long invoiceId,
            @RequestHeader("X-User-Id") Long userId
    ) {
        return billingService.getInvoiceDetailForCustomer(invoiceId, userId);
    }
}