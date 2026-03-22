package com.trademind.billing.serviceImpl;

import com.trademind.billing.dto.InvoiceResponseDto;
import com.trademind.billing.dto.InvoiceSummaryDto;
import com.trademind.billing.entity.Invoice;
import com.trademind.billing.enums.SourceType;
import com.trademind.billing.mapper.InvoiceMapper;
import com.trademind.billing.repository.InvoiceRepository;
import com.trademind.billing.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BillingServiceImpl implements BillingService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    // ============================================================
    // CUSTOMER (Buyer)
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceSummaryDto> getMyInvoices(
            Long userId,
            Pageable pageable
    ) {
        return invoiceRepository.findByUserId(userId, pageable)
                .map(invoiceMapper::toSummaryDto);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponseDto getInvoiceDetailForCustomer(
            Long invoiceId,
            Long userId
    ) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalStateException("Invoice not found"));

        if (!invoice.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized access");
        }

        return invoiceMapper.toResponseDto(invoice);
    }

    // ============================================================
    // SELLER (Merchant / Retailer)
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceSummaryDto> getSellerInvoices(
            Long sourceId,
            SourceType sourceType,
            Pageable pageable
    ) {
        return invoiceRepository
                .findBySourceIdAndSourceType(sourceId, sourceType, pageable)
                .map(invoiceMapper::toSummaryDto);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponseDto getInvoiceDetailForSeller(
            Long invoiceId,
            Long sourceId
    ) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalStateException("Invoice not found"));

        if (!invoice.getSourceId().equals(sourceId)) {
            throw new IllegalStateException("Unauthorized access");
        }

        return invoiceMapper.toResponseDto(invoice);
    }

    // ============================================================
    // ADMIN
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceSummaryDto> getAllInvoices(Pageable pageable) {
        return invoiceRepository.findAll(pageable)
                .map(invoiceMapper::toSummaryDto);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponseDto getInvoiceDetailForAdmin(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalStateException("Invoice not found"));

        return invoiceMapper.toResponseDto(invoice);
    }
}