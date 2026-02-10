package com.trademind.user.controller;

import com.trademind.user.dto.AdminProfileDto;
import com.trademind.user.service.AdminProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/admin/profile")
@RequiredArgsConstructor
public class AdminProfileController {

    private final AdminProfileService service;

    @GetMapping
    public AdminProfileDto getProfile(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestHeader("X-USER-ROLE") String role) {

        if (!role.equals("ADMIN")) {
            throw new RuntimeException("Access denied");
        }

        return service.getMyAdminProfile(userId);
    }
}
