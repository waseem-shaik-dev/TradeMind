package com.trademind.user.repository;

import com.trademind.user.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByUser_Id(Long userId);


    @Query("""
        select up 
        from UserProfile up
        where up.user.id = :userId
    """)
    Optional<UserProfile> findByUserId(Long userId);

    boolean existsByUser_Id(Long userId);

    // Useful for search / support tools
    Optional<UserProfile> findByEmail(String email);
    Optional<UserProfile> findByPhone(String phone);
}
