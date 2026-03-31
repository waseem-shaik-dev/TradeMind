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
}
