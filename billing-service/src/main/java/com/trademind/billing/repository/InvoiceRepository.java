package com.trademind.billing.repository;

import com.trademind.billing.dto.AdminRevenueSummaryDto;
import com.trademind.billing.dto.RevenueTrendDto;
import com.trademind.billing.dto.TopMerchantRawDto;
import com.trademind.billing.dto.TopPerformerDto;
import com.trademind.billing.entity.Invoice;
import com.trademind.billing.enums.SourceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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


    // ================= TOTAL REVENUE =================
    @Query("""
        SELECT COALESCE(SUM(i.grandTotal), 0)
        FROM Invoice i
        WHERE i.paymentStatus = 'PAID'
    """)
    BigDecimal getTotalRevenue();


    // ================= MERCHANT REVENUE =================
    @Query("""
        SELECT COALESCE(SUM(i.grandTotal), 0)
        FROM Invoice i
        WHERE i.sourceId = :merchantId AND i.sourceType = 'MERCHANT'
        AND i.paymentStatus = 'PAID'
    """)
    BigDecimal getRevenueByMerchant(@Param("merchantId") Long merchantId);


    // ================= RETAILER REVENUE =================
    @Query("""
        SELECT COALESCE(SUM(i.grandTotal), 0)
        FROM Invoice i
        WHERE i.sourceId = :retailerId AND i.sourceType = 'RETAILER'
        AND i.paymentStatus = 'PAID'
    """)
    BigDecimal getRevenueByRetailer(@Param("retailerId") Long retailerId);


    // ================= CUSTOMER SPENT =================
    @Query("""
        SELECT COALESCE(SUM(i.grandTotal), 0)
        FROM Invoice i
        WHERE i.userId = :customerId
        AND i.paymentStatus = 'PAID'
    """)
    BigDecimal getTotalSpentByCustomer(@Param("customerId") Long customerId);


    // ================= TODAY SALES =================
    @Query("""
        SELECT COALESCE(SUM(i.grandTotal), 0)
        FROM Invoice i
        WHERE i.sourceId = :retailerId
        AND i.paymentStatus = 'PAID'
        AND i.createdAt >= :startOfDay
    """)
    BigDecimal getTodaySales(
            @Param("retailerId") Long retailerId,
            @Param("startOfDay") LocalDateTime startOfDay
    );


    // ================= REVENUE TREND =================
    @Query("""
        SELECT new com.trademind.billing.dto.RevenueTrendDto(
            CAST(i.createdAt as string),
            CAST(SUM(i.grandTotal) as string)
        )
        FROM Invoice i
        WHERE i.createdAt >= :startDate
        AND i.paymentStatus = 'PAID'
        GROUP BY i.createdAt
        ORDER BY i.createdAt
    """)
    List<RevenueTrendDto> getRevenueTrend(@Param("startDate") LocalDateTime startDate);



    @Query("""
    SELECT new com.trademind.billing.dto.TopMerchantRawDto(
        i.sourceId,
        COUNT(i),
        CAST(SUM(i.grandTotal) as string)
    )
    FROM Invoice i
    WHERE i.paymentStatus = 'PAID' AND i.sourceType = 'MERCHANT'
    GROUP BY i.sourceId
    ORDER BY SUM(i.grandTotal) DESC
""")
    List<TopMerchantRawDto> getTopMerchants(Pageable pageable);

    @Query("""
    SELECT COALESCE(SUM(i.grandTotal), 0)
    FROM Invoice i
    WHERE i.paymentStatus = 'PAID'
    AND i.createdAt BETWEEN :start AND :end
""")
    BigDecimal getRevenueBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
    SELECT 
        FUNCTION('DATE', i.createdAt),
        SUM(i.grandTotal)
    FROM Invoice i
    WHERE i.paymentStatus = 'PAID'
    AND i.createdAt BETWEEN :start AND :end
    GROUP BY FUNCTION('DATE', i.createdAt)
    ORDER BY FUNCTION('DATE', i.createdAt)
""")
    List<Object[]> getDailyRevenue(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
    SELECT new com.trademind.billing.dto.AdminRevenueSummaryDto(
        CAST(SUM(i.grandTotal) as string),
        CAST(SUM(CASE WHEN i.sourceType = 'MERCHANT' THEN i.grandTotal ELSE 0 END) as string),
        CAST(SUM(CASE WHEN i.sourceType = 'RETAILER' THEN i.grandTotal ELSE 0 END) as string),
        CAST(SUM(CASE WHEN i.userId IS NOT NULL THEN i.grandTotal ELSE 0 END) as string)
    )
    FROM Invoice i
    WHERE i.paymentStatus = 'PAID'
""")
    AdminRevenueSummaryDto getAdminRevenueSummary();
}