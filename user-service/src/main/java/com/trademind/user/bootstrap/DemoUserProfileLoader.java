package com.trademind.user.bootstrap;

import com.trademind.user.entity.*;
import com.trademind.user.enums.UserRole;
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
                            merchant.setGstNumber("29ABCDE1234F1Z5");
                            merchant.setLicenseNumber("LIC-DEMO-001");

                            // 📍 LOCATION
                            merchant.setLatitude(17.385044);
                            merchant.setLongitude(78.486671);
                            merchant.setMapUrl(
                                    "https://maps.google.com/?q=17.385044,78.486671"
                            );

                            // 🔒 DEFAULT
                            merchant.setVerified(false);
                        });

                case RETAILER -> retailerProfileRepository
                        .findByUser_Id(user.getId())
                        .ifPresent(retailer -> {

                            retailer.setShopName("Demo Retail Shop");
                            retailer.setShopEmail("retailer@demo.com");
                            retailer.setGstNumber("29ABCDE9999F1Z5");

                            // 📍 LOCATION
                            retailer.setLatitude(17.387140);
                            retailer.setLongitude(78.491684);
                            retailer.setMapUrl(
                                    "https://maps.google.com/?q=17.387140,78.491684"
                            );

                            // 🔒 DEFAULT
                            retailer.setVerified(false);
                        });
            }
        });
    }
}
