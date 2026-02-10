package com.trademind.user.repository;

import com.trademind.user.entity.AdminProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminProfileRepository extends JpaRepository<AdminProfile, Long> {

    Optional<AdminProfile> findByUser_Id(Long userId);

    boolean existsByUser_Id(Long userId);

    // Useful for org hierarchy later
    Optional<AdminProfile> findByDepartment(String department);
}
