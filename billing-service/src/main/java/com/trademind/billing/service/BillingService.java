package com.trademind.billing.service;

import com.trademind.billing.dto.InvoiceResponseDto;
import com.trademind.billing.dto.InvoiceSummaryDto;
import com.trademind.billing.dto.RevenueGraphDto;
import com.trademind.billing.enums.SourceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BillingService {

    // ============================================================
    // CUSTOMER (Buyer)
    // ============================================================

    Page<InvoiceSummaryDto> getMyInvoices(
            Long userId,
            Pageable pageable
    );

    InvoiceResponseDto getInvoiceDetailForCustomer(
            Long invoiceId,
            Long userId
    );


    // ============================================================
    // SELLER (Merchant / Retailer)
    // ============================================================

    Page<InvoiceSummaryDto> getSellerInvoices(
            Long sourceId,
            SourceType sourceType,
            Pageable pageable
    );

    InvoiceResponseDto getInvoiceDetailForSeller(
            Long invoiceId,
            Long sourceId
    );


    // ============================================================
    // ADMIN
    // ============================================================

    Page<InvoiceSummaryDto> getAllInvoices(Pageable pageable);

    InvoiceResponseDto getInvoiceDetailForAdmin(Long invoiceId);

}