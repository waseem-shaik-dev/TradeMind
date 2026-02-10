package com.trademind.auth.bootstrap;

import com.trademind.auth.dto.RegisterRequest;
import com.trademind.auth.enums.Role;
import com.trademind.auth.repository.AuthUserRepository;
import com.trademind.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("dev") // 👈 NEVER run in prod
public class DemoAuthDataLoader implements CommandLineRunner {

    private final AuthUserRepository authUserRepository;
    private final AuthService authService;

    @Override
    public void run(String... args) {

        // ✅ RUN ONLY IF AUTH TABLE IS EMPTY
        if (authUserRepository.count() > 0) {
            return;
        }

        // 🔐 ADMIN (first → predictable ID order)
        authService.register(
                new RegisterRequest(
                        "admin_demo",
                        "admin@trademind.com",
                        "password",
                        Role.ADMIN
                ),
                "ADMIN" // bootstrap override
        );

        // 👤 CUSTOMER
        authService.register(
                new RegisterRequest(
                        "customer_demo",
                        "customer@trademind.com",
                        "password",
                        Role.CUSTOMER
                ),
                "ADMIN"
        );

        // 🏬 MERCHANT
        authService.register(
                new RegisterRequest(
                        "merchant_demo",
                        "merchant@trademind.com",
                        "password",
                        Role.MERCHANT
                ),
                "ADMIN"
        );

        // 🏪 RETAILER
        authService.register(
                new RegisterRequest(
                        "retailer_demo",
                        "retailer@trademind.com",
                        "password",
                        Role.RETAILER
                ),
                "ADMIN"
        );
    }
}
