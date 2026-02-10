package com.trademind.user.controller;

import com.trademind.user.dto.CustomerProfileDto;
import com.trademind.user.service.CustomerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/customer/profile")
@RequiredArgsConstructor
public class CustomerProfileController {

    private final CustomerProfileService service;

    @GetMapping
    public CustomerProfileDto getProfile(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestHeader("X-USER-ROLE") String role) {

        if (!role.equals("CUSTOMER")) {
            throw new RuntimeException("Access denied");
        }

        return service.getMyCustomerProfile(userId);
    }


}
