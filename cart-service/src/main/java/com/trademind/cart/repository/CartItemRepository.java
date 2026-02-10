package com.trademind.cart.repository;

import com.trademind.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // ----------------------------------------------------
    // BASIC FETCHES
    // ----------------------------------------------------

    List<CartItem> findAllByCartId(Long cartId);

    Optional<CartItem> findByIdAndCartId(Long cartItemId, Long cartId);

    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

    boolean existsByCartIdAndProductId(Long cartId, Long productId);

    // ----------------------------------------------------
    // AGGREGATIONS (PERFORMANCE)
    // ----------------------------------------------------

    @Query("""
        SELECT SUM(ci.quantity)
        FROM CartItem ci
        WHERE ci.cart.id = :cartId
    """)
    Integer getTotalQuantity(Long cartId);

    @Query("""
        SELECT COUNT(ci)
        FROM CartItem ci
        WHERE ci.cart.id = :cartId
    """)
    Integer getTotalItems(Long cartId);

    // ----------------------------------------------------
    // BULK OPERATIONS
    // ----------------------------------------------------

    @Modifying
    @Query("""
        DELETE FROM CartItem ci
        WHERE ci.cart.id = :cartId
    """)
    void deleteAllByCartId(Long cartId);

    // ----------------------------------------------------
    // VALIDATION
    // ----------------------------------------------------

    boolean existsByIdAndCartId(Long cartItemId, Long cartId);
}
