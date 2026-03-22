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
@RequestMapping("/api/billing/retailer")
@RequiredArgsConstructor
public class RetailerBillingController {

    private final BillingService billingService;

    // Retailer SALES (customers buying from retailer)
    @GetMapping("/sales")
    public Page<InvoiceSummaryDto> getRetailerSalesInvoices(
            @RequestHeader("X-User-Id") Long retailerId,
            Pageable pageable
    ) {
        return billingService.getSellerInvoices(
                retailerId,
                SourceType.RETAILER,
                pageable
        );
    }

    // Retailer PURCHASES (retailer buying from merchants)
    @GetMapping("/purchases")
    public Page<InvoiceSummaryDto> getRetailerPurchaseInvoices(
            @RequestHeader("X-User-Id") Long retailerId,
            Pageable pageable
    ) {
        return billingService.getMyInvoices(retailerId, pageable);
    }

    @GetMapping("/{invoiceId}")
    public InvoiceResponseDto getInvoiceDetail(
            @PathVariable Long invoiceId,
            @RequestHeader("X-User-Id") Long retailerId
    ) {
        // Works for both purchase & sales (validation handled in service)
        return billingService.getInvoiceDetailForSeller(invoiceId, retailerId);
    }
}