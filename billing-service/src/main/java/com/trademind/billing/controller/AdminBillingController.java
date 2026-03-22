package com.trademind.billing.controller;

import com.trademind.billing.dto.InvoiceResponseDto;
import com.trademind.billing.dto.InvoiceSummaryDto;
import com.trademind.billing.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billing/admin")
@RequiredArgsConstructor
public class AdminBillingController {

    private final BillingService billingService;

    @GetMapping
    public Page<InvoiceSummaryDto> getAllInvoices(Pageable pageable) {
        return billingService.getAllInvoices(pageable);
    }

    @GetMapping("/{invoiceId}")
    public InvoiceResponseDto getInvoiceDetail(
            @PathVariable Long invoiceId
    ) {
        return billingService.getInvoiceDetailForAdmin(invoiceId);
    }
}