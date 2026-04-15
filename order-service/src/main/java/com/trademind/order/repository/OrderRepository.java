package com.trademind.order.repository;

import com.trademind.order.dto.response.OrderCountResponse;
import com.trademind.order.dto.response.RecentOrderDto;
import com.trademind.order.dto.view.OrderFullViewDto;
import com.trademind.order.entity.Order;
import com.trademind.order.enums.BuyerType;
import com.trademind.order.enums.OrderStatus;
import com.trademind.order.enums.SourceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // -------------------------------------------------------
    // Idempotency / Kafka Safety
    // -------------------------------------------------------

    Optional<Order> findByCheckoutId(Long checkoutId);

    boolean existsByCheckoutId(Long checkoutId);


    // -------------------------------------------------------
    // CUSTOMER APIs
    // -------------------------------------------------------

    Page<Order> findByUserId(Long userId, Pageable pageable);

    Page<Order> findByUserIdAndOrderStatus(
            Long userId,
            OrderStatus orderStatus,
            Pageable pageable
    );


    // -------------------------------------------------------
    // MERCHANT / RETAILER APIs
    // -------------------------------------------------------

    Page<Order> findBySourceIdAndSourceType(
            Long sourceId,
            SourceType sourceType,
            Pageable pageable
    );

    Page<Order> findBySourceIdAndSourceTypeAndOrderStatus(
            Long sourceId,
            SourceType sourceType,
            OrderStatus orderStatus,
            Pageable pageable
    );


    // -------------------------------------------------------
    // ADMIN APIs
    // -------------------------------------------------------

    Page<Order> findByOrderStatus(
            OrderStatus orderStatus,
            Pageable pageable
    );

    Page<Order> findByCreatedAtBetween(
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

    Page<Order> findByUserIdAndCreatedAtBetween(
            Long userId,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );


    // -------------------------------------------------------
    // DASHBOARD QUICK STATS
    // -------------------------------------------------------

    long countByOrderStatus(OrderStatus status);

    long countBySourceIdAndSourceTypeAndOrderStatus(
            Long sourceId,
            SourceType sourceType,
            OrderStatus status
    );


    // -------------------------------------------------------
    // Advanced Fetch with Line Items (optional optimization)
    // -------------------------------------------------------

    @Query("""
           SELECT o FROM Order o
           LEFT JOIN FETCH o.lineItems
           LEFT JOIN FETCH o.addressSnapshot
           WHERE o.id = :orderId
           """)
    Optional<Order> findDetailedById(Long orderId);


    // -------------------------------------------------------
    // Bulk Update Safety Check Queries
    // -------------------------------------------------------

    List<Order> findByOrderStatusIn(List<OrderStatus> statuses);

    Page<Order> findByUserIdAndBuyerType(
            Long userId,
            BuyerType buyerType,
            Pageable pageable
    );


    // ================= GLOBAL COUNT =================
    @Query("""
        SELECT new com.trademind.order.dto.response.OrderCountResponse(
            COUNT(o),
            COALESCE(SUM(CASE WHEN o.orderStatus = 'PENDING' THEN 1 ELSE 0 END), 0),
            COALESCE(SUM(CASE WHEN o.orderStatus = 'DELIVERED' THEN 1 ELSE 0 END), 0)
        )
        FROM Order o
    """)
    OrderCountResponse getGlobalOrderStats();


    // ================= BY MERCHANT =================
    @Query("""
        SELECT new com.trademind.order.dto.response.OrderCountResponse(
            COUNT(o),
            COALESCE(SUM(CASE WHEN o.orderStatus = 'PENDING' THEN 1 ELSE 0 END), 0),
            COALESCE(SUM(CASE WHEN o.orderStatus = 'DELIVERED' THEN 1 ELSE 0 END), 0)
        )
        FROM Order o
        WHERE o.sourceId = :merchantId
    """)
    OrderCountResponse getMerchantOrderStats(@Param("merchantId") Long merchantId);


    // ================= BY RETAILER =================
    @Query("""
        SELECT new com.trademind.order.dto.response.OrderCountResponse(
            COUNT(o),
            COALESCE(SUM(CASE WHEN o.orderStatus = 'PENDING' THEN 1 ELSE 0 END), 0),
            COALESCE(SUM(CASE WHEN o.orderStatus = 'DELIVERED' THEN 1 ELSE 0 END), 0)
        )
        FROM Order o
        WHERE o.sourceId = :retailerId
    """)
    OrderCountResponse getRetailerOrderStats(@Param("retailerId") Long retailerId);


    // ================= BY CUSTOMER =================
    @Query("""
          SELECT new com.trademind.order.dto.response.OrderCountResponse(
            COUNT(o),
            COALESCE(SUM(CASE WHEN o.orderStatus = 'PENDING' THEN 1 ELSE 0 END), 0),
            COALESCE(SUM(CASE WHEN o.orderStatus = 'DELIVERED' THEN 1 ELSE 0 END), 0)
        )
        FROM Order o
        WHERE o.userId = :customerId
    """)
    OrderCountResponse getCustomerOrderStats(@Param("customerId") Long customerId);


    // ================= ACTIVE ORDERS =================
    @Query("""
        SELECT COUNT(o)
        FROM Order o
        WHERE o.userId = :customerId
        AND o.orderStatus IN ('PENDING', 'CONFIRMED', 'SHIPPED')
    """)
    long getActiveOrders(@Param("customerId") Long customerId);


    // ================= REVENUE =================
    @Query("""
        SELECT COALESCE(SUM(o.grandTotal), 0)
        FROM Order o
    """)
    java.math.BigDecimal getTotalRevenue();


    @Query("""
        SELECT COALESCE(SUM(o.grandTotal), 0)
        FROM Order o
        WHERE o.sourceId = :merchantId
    """)
    java.math.BigDecimal getRevenueByMerchant(@Param("merchantId") Long merchantId);


    @Query("""
        SELECT COALESCE(SUM(o.grandTotal), 0)
        FROM Order o
        WHERE o.userId = :retailerId
    """)
    java.math.BigDecimal getRevenueByRetailer(@Param("retailerId") Long retailerId);


    @Query("""
        SELECT COALESCE(SUM(o.grandTotal), 0)
        FROM Order o
        WHERE o.userId = :customerId
    """)
    java.math.BigDecimal getRevenueByCustomer(@Param("customerId") Long customerId);


    // ================= RECENT ORDERS =================
    @Query("""
        SELECT new com.trademind.order.dto.response.RecentOrderDto(
            o.orderNumber,
            o.userId,
            CAST(o.grandTotal as string),
            CAST(o.orderStatus as string),
            CAST(o.createdAt as string)
        )
        FROM Order o
        WHERE o.userId = :userId
        ORDER BY o.createdAt DESC
    """)
    List<RecentOrderDto> getRecentOrders(@Param("userId") Long userId, Pageable pageable);

    @Query("""
        SELECT new com.trademind.order.dto.response.RecentOrderDto(
            o.orderNumber,
            o.userId,
            CAST(o.grandTotal as string),
            CAST(o.orderStatus as string),
            CAST(o.createdAt as string)
        )
        FROM Order o
        WHERE o.sourceId = :sellerId
        ORDER BY o.createdAt DESC
    """)
    List<RecentOrderDto> getRecentOrdersForSeller(@Param("sellerId") Long sellerId, Pageable pageable);

    @Query("""
    SELECT new com.trademind.order.dto.response.OrderCountResponse(
        COUNT(o),
        COALESCE(SUM(CASE WHEN o.orderStatus = 'PENDING' THEN 1 ELSE 0 END), 0),
        COALESCE(SUM(CASE WHEN o.orderStatus = 'DELIVERED' THEN 1 ELSE 0 END), 0)
    )
    FROM Order o
    WHERE o.createdAt BETWEEN :start AND :end
""")
    OrderCountResponse getOrderStatsBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
    SELECT new com.trademind.order.dto.response.OrderCountResponse(
        COUNT(o),
        COALESCE(SUM(CASE WHEN o.orderStatus = 'PENDING' THEN 1 ELSE 0 END), 0),
        COALESCE(SUM(CASE WHEN o.orderStatus = 'DELIVERED' THEN 1 ELSE 0 END), 0)
    )
    FROM Order o
    WHERE o.sourceId = :merchantId
    AND o.createdAt BETWEEN :start AND :end
""")
    OrderCountResponse getMerchantOrdersBetween(
            @Param("merchantId") Long merchantId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
    SELECT new com.trademind.order.dto.response.OrderCountResponse(
        COUNT(o),
        COALESCE(SUM(CASE WHEN o.orderStatus = 'PENDING' THEN 1 ELSE 0 END), 0),
        COALESCE(SUM(CASE WHEN o.orderStatus = 'DELIVERED' THEN 1 ELSE 0 END), 0)
    )
    FROM Order o
    WHERE o.sourceId = :retailerId
    AND o.createdAt BETWEEN :start AND :end
""")
    OrderCountResponse getRetailerOrdersBetween(
            @Param("retailerId") Long retailerId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
    SELECT new com.trademind.order.dto.response.OrderCountResponse(
        COUNT(o),
        COALESCE(SUM(CASE WHEN o.orderStatus = 'PENDING' THEN 1 ELSE 0 END), 0),
        COALESCE(SUM(CASE WHEN o.orderStatus = 'DELIVERED' THEN 1 ELSE 0 END), 0)
    )
    FROM Order o
    WHERE o.userId = :customerId
    AND o.createdAt BETWEEN :start AND :end
""")
    OrderCountResponse getCustomerOrdersBetween(
            @Param("customerId") Long customerId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );



    @Query("""
    SELECT FUNCTION('DATE', o.createdAt), COUNT(o)
    FROM Order o
    WHERE o.createdAt >= :start
    AND (:sourceId IS NULL OR o.sourceId = :sourceId)
    AND (:userId IS NULL OR o.userId = :userId)
    GROUP BY FUNCTION('DATE', o.createdAt)
    ORDER BY FUNCTION('DATE', o.createdAt)
""")
    List<Object[]> getOrderGraph(
            @Param("start") LocalDateTime start,
            @Param("sourceId") Long sourceId,
            @Param("userId") Long userId
    );


}
