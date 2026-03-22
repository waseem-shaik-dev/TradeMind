package com.trademind.billing.controller;

import com.trademind.billing.dto.InvoiceResponseDto;
import com.trademind.billing.dto.InvoiceSummaryDto;
import com.trademind.billing.enums.SourceType;
import com.trademind.billing.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billing/merchant")
@RequiredArgsConstructor
public class MerchantBillingController {

    private final BillingService billingService;

    @GetMapping
    public Page<InvoiceSummaryDto> getMerchantInvoices(
            @RequestHeader("X-User-Id") Long merchantId,
            Pageable pageable
    ) {
        return billingService.getSellerInvoices(
                merchantId,
                SourceType.MERCHANT,
                pageable
        );
    }

    @GetMapping("/{invoiceId}")
    public InvoiceResponseDto getInvoiceDetail(
            @PathVariable Long invoiceId,
            @RequestHeader("X-User-Id") Long merchantId
    ) {
        return billingService.getInvoiceDetailForSeller(invoiceId, merchantId);
    }
}