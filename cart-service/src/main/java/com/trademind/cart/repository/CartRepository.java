package com.trademind.cart.repository;

import com.trademind.cart.entity.Cart;
import com.trademind.cart.enums.CartStatus;
import com.trademind.cart.enums.SourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // ----------------------------------------------------
    // BASIC FETCHES
    // ----------------------------------------------------

    Optional<Cart> findByIdAndUserId(Long cartId, Long userId);

    List<Cart> findAllByUserId(Long userId);

    List<Cart> findAllByUserIdAndActiveTrue(Long userId);

    // ----------------------------------------------------
    // SOURCE-SPECIFIC CART
    // ----------------------------------------------------

    Optional<Cart> findByUserIdAndSourceIdAndSourceTypeAndStatus(
            Long userId,
            Long sourceId,
            SourceType sourceType,
            CartStatus status
    );

    boolean existsByUserIdAndSourceIdAndSourceTypeAndStatus(
            Long userId,
            Long sourceId,
            SourceType sourceType,
            CartStatus status
    );

    // ----------------------------------------------------
    // CART LIMIT ENFORCEMENT (MAX 10)
    // ----------------------------------------------------

    long countByUserIdAndActiveTrue(Long userId);

    // ----------------------------------------------------
    // BULK / MAINTENANCE
    // ----------------------------------------------------

    @Query("""
        SELECT c
        FROM Cart c
        WHERE c.userId = :userId
          AND c.status = 'ACTIVE'
        ORDER BY c.updatedAt DESC
    """)
    List<Cart> findActiveCartsOrdered(
            @Param("userId") Long userId
    );

    @Query("""
        SELECT c
        FROM Cart c
        WHERE c.status = :status
    """)
    List<Cart> findByStatus(
            @Param("status") CartStatus status
    );

    // ----------------------------------------------------
    // SECURITY / VALIDATION
    // ----------------------------------------------------

    boolean existsByIdAndUserId(Long cartId, Long userId);
}
