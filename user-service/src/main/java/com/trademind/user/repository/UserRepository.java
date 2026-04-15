package com.trademind.user.repository;

import com.trademind.user.dto.UserCountResponse;
import com.trademind.user.entity.User;
import com.trademind.user.enums.UserRole;
import com.trademind.user.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Basic existence
    boolean existsById(Long id);

    // Role-based queries
    List<User> findByRole(UserRole role);
    List<User> findByRoleAndStatus(UserRole role, UserStatus status);

    // Status-based
    List<User> findByStatus(UserStatus status);

    // Admin dashboards / analytics
    long countByRole(UserRole role);
    long countByStatus(UserStatus status);

    @Query("""
    SELECT new com.trademind.user.dto.UserCountResponse(
        COALESCE(SUM(CASE WHEN u.role = 'MERCHANT' THEN 1 ELSE 0 END), 0),
        COALESCE(SUM(CASE WHEN u.role = 'RETAILER' THEN 1 ELSE 0 END), 0),
        COALESCE(SUM(CASE WHEN u.role = 'CUSTOMER' THEN 1 ELSE 0 END), 0)
    )
    FROM User u
""")
    UserCountResponse countUsersByRole();


    // Growth data
    @Query("""
        SELECT DATE(u.createdAt), COUNT(u)
        FROM User u
        WHERE u.createdAt >= :startDate
        GROUP BY DATE(u.createdAt)
        ORDER BY DATE(u.createdAt)
    """)
    List<Object[]> getUserGrowth(@Param("startDate") LocalDateTime startDate);

    @Query("""
    SELECT new com.trademind.user.dto.UserCountResponse(
        COALESCE(SUM(CASE WHEN u.role = 'MERCHANT' THEN 1 ELSE 0 END), 0),
        COALESCE(SUM(CASE WHEN u.role = 'RETAILER' THEN 1 ELSE 0 END), 0),
        COALESCE(SUM(CASE WHEN u.role = 'CUSTOMER' THEN 1 ELSE 0 END), 0)
    )
    FROM User u
    WHERE u.createdAt BETWEEN :start AND :end
""")
    UserCountResponse countUsersByRoleBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
    SELECT FUNCTION('DATE', u.createdAt),
        SUM(CASE WHEN u.role = 'MERCHANT' THEN 1 ELSE 0 END),
        SUM(CASE WHEN u.role = 'RETAILER' THEN 1 ELSE 0 END),
        SUM(CASE WHEN u.role = 'CUSTOMER' THEN 1 ELSE 0 END)
    FROM User u
    WHERE u.createdAt >= :start
    GROUP BY FUNCTION('DATE', u.createdAt)
    ORDER BY FUNCTION('DATE', u.createdAt)
""")
    List<Object[]> getUserGraph(@Param("start") LocalDateTime start);

    @Query("""
        SELECT u FROM User u
        LEFT JOIN FETCH u.profile p
        LEFT JOIN FETCH u.addresses a
        LEFT JOIN FETCH u.adminProfile ap
        LEFT JOIN FETCH u.customerProfile cp
        LEFT JOIN FETCH u.merchantProfile mp
        LEFT JOIN FETCH mp.storeAddress msa
        LEFT JOIN FETCH u.retailerProfile rp
        LEFT JOIN FETCH rp.storeAddress rsa
        WHERE u.id = :userId
    """)
    Optional<User> findCompleteUserById(Long userId);
}
