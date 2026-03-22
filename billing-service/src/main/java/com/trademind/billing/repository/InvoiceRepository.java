package com.trademind.billing.repository;

import com.trademind.billing.entity.Invoice;
import com.trademind.billing.enums.SourceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    // -------------------------------------------------------
    // Idempotency
    // -------------------------------------------------------

    boolean existsByOrderId(Long orderId);

    Optional<Invoice> findByOrderId(Long orderId);


    // -------------------------------------------------------
    // CUSTOMER (buyer)
    // -------------------------------------------------------

    Page<Invoice> findByUserId(Long userId, Pageable pageable);


    // -------------------------------------------------------
    // SELLER (merchant / retailer)
    // -------------------------------------------------------

    Page<Invoice> findBySourceIdAndSourceType(
            Long sourceId,
            SourceType sourceType,
            Pageable pageable
    );


    // -------------------------------------------------------
    // ADMIN (all invoices)
    // -------------------------------------------------------

}