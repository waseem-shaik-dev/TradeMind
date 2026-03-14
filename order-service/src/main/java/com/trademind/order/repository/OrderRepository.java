package com.trademind.order.repository;

import com.trademind.order.dto.view.OrderFullViewDto;
import com.trademind.order.entity.Order;
import com.trademind.order.enums.BuyerType;
import com.trademind.order.enums.OrderStatus;
import com.trademind.order.enums.SourceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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



}
