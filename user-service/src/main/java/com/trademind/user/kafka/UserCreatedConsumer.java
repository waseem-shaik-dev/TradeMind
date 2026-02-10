package com.trademind.user.kafka;


import com.trademind.events.UserCreatedEvent;
import com.trademind.user.entity.*;
import com.trademind.user.enums.UserRole;
import com.trademind.user.enums.UserStatus;
import com.trademind.user.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserCreatedConsumer {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final AdminProfileRepository adminProfileRepository;
    private final CustomerProfileRepository customerProfileRepository;
    private final MerchantProfileRepository merchantProfileRepository;
    private final RetailerProfileRepository retailerProfileRepository;

    @KafkaListener(
            topics = "USER_CREATED_TOPIC",
            groupId = "user-service"
    )
    @Transactional
    public void consume(UserCreatedEvent event) {

        if (event == null || event.userId() == null || event.role() == null) {
            return;
        }


        // 🔁 Idempotency check (VERY IMPORTANT)
        if (userRepository.existsById(event.userId())) {
            return;
        }

        UserRole role = UserRole.valueOf(event.role().toUpperCase());

        // 1️⃣ Create root USER
        User user = User.builder()
                .id(event.userId())
                .role(role)
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();



        // 2️⃣ Create COMMON USER PROFILE
        UserProfile userProfile = UserProfile.builder()
                .user(user)
                .fullName(event.username())
                .email(event.email())
                .build();

        user.setProfile(userProfile);

        // 3️⃣ Create ROLE-SPECIFIC PROFILE
        switch (role) {

            case ADMIN -> createAdminProfile(user);

            case CUSTOMER -> createCustomerProfile(user);

            case MERCHANT -> createMerchantProfile(user);

            case RETAILER -> createRetailerProfile(user);
        }

        userRepository.save(user);
    }

    /* ==========================
       ROLE-SPECIFIC INITIALIZERS
       ========================== */

    private void createAdminProfile(User user) {
        AdminProfile adminProfile = AdminProfile.builder()
                .user(user)
                .department("SYSTEM")
                .designation("ADMIN")
                .build();

        user.setAdminProfile(adminProfile);
    }

    private void createCustomerProfile(User user) {
        CustomerProfile customerProfile = CustomerProfile.builder()
                .user(user)
                .loyaltyPoints(0)
                .newsletterSubscribed(false)
                .build();

        user.setCustomerProfile(customerProfile);
    }

    private void createMerchantProfile(User user) {
        MerchantProfile merchantProfile = MerchantProfile.builder()
                .user(user)
                .verified(false)
                .build();

        user.setMerchantProfile(merchantProfile);
    }

    private void createRetailerProfile(User user) {
        RetailerProfile retailerProfile = RetailerProfile.builder()
                .user(user)
                .verified(false)
                .build();

        user.setRetailerProfile(retailerProfile);
    }

}
