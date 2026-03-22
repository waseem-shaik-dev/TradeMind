package com.trademind.user.bootstrap;

import com.trademind.user.entity.*;
import com.trademind.user.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class DemoUserProfileLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final AdminProfileRepository adminProfileRepository;
    private final CustomerProfileRepository customerProfileRepository;
    private final MerchantProfileRepository merchantProfileRepository;
    private final RetailerProfileRepository retailerProfileRepository;

    @Override
    @Transactional
    public void run(String... args) {

        // ✅ RUN ONLY IF NO ROLE PROFILES EXIST
        if (merchantProfileRepository.count() > 0
                || retailerProfileRepository.count() > 0) {
            return;
        }

        userRepository.findAll().forEach(user -> {

            UserProfile profile = userProfileRepository
                    .findByUser_Id(user.getId())
                    .orElse(null);

            if (profile == null) return;

            // --------------------
            // COMMON PROFILE
            // --------------------
            profile.setFullName(user.getRole() + " Demo User");
            profile.setPhone("9999999999");

            // --------------------
            // ROLE-SPECIFIC
            // --------------------
            switch (user.getRole()) {

                case ADMIN -> adminProfileRepository
                        .findByUser_Id(user.getId())
                        .ifPresent(admin -> {
                            admin.setDepartment("PLATFORM");
                            admin.setDesignation("SUPER_ADMIN");
                        });

                case CUSTOMER -> customerProfileRepository
                        .findByUser_Id(user.getId())
                        .ifPresent(customer -> {
                            customer.setLoyaltyPoints(100);
                            customer.setNewsletterSubscribed(true);
                        });

                case MERCHANT -> merchantProfileRepository
                        .findByUser_Id(user.getId())
                        .ifPresent(merchant -> {

                            merchant.setBusinessName("Demo Merchant Pvt Ltd");
                            merchant.setBusinessEmail("merchant@demo.com");

                        });

                case RETAILER -> retailerProfileRepository
                        .findByUser_Id(user.getId())
                        .ifPresent(retailer -> {

                            retailer.setShopName("Demo Retail Shop");
                            retailer.setShopEmail("retailer@demo.com");

                        });
            }
        });
    }
}
