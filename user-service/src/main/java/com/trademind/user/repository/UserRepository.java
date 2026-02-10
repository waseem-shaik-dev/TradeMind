package com.trademind.user.repository;

import com.trademind.user.entity.User;
import com.trademind.user.enums.UserRole;
import com.trademind.user.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
