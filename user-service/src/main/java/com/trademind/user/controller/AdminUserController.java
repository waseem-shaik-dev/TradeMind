package com.trademind.user.controller;

import com.trademind.user.dto.MerchantProfileDto;
import com.trademind.user.dto.RetailerProfileDto;
import com.trademind.user.dto.UserResponseDto;
import com.trademind.user.enums.UserRole;
import com.trademind.user.enums.UserStatus;
import com.trademind.user.service.AdminUserService;
import com.trademind.user.service.MerchantProfileService;
import com.trademind.user.service.RetailerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;
    private final MerchantProfileService merchantProfileService;
    private final RetailerProfileService retailerProfileService;

    /* =========================
       USER LISTING APIs
       ========================= */

    @GetMapping
    public List<UserResponseDto> getAllUsers(
            @RequestHeader("X-USER-ROLE") String role
    ) {
        validateAdmin(role);
        return adminUserService.getAllUsers();
    }

    @GetMapping("/role/{role}")
    public List<UserResponseDto> getUsersByRole(
            @PathVariable UserRole role,
            @RequestHeader("X-USER-ROLE") String requesterRole
    ) {
        validateAdmin(requesterRole);
        return adminUserService.getUsersByRole(role);
    }

    /* =========================
       PROFILE EDIT (ADMIN)
       ========================= */

    /**
     * ADMIN: Edit Merchant profile by userId
     */
    @PutMapping("/{userId}/merchant-profile")
    public MerchantProfileDto updateMerchantProfile(
            @PathVariable Long userId,
            @RequestBody MerchantProfileDto dto,
            @RequestHeader("X-USER-ROLE") String role
    ) {
        validateAdmin(role);
        return merchantProfileService.updateMyProfile(userId, dto);
    }

    /**
     * ADMIN: Edit Retailer profile by userId
     */
    @PutMapping("/{userId}/retailer-profile")
    public RetailerProfileDto updateRetailerProfile(
            @PathVariable Long userId,
            @RequestBody RetailerProfileDto dto,
            @RequestHeader("X-USER-ROLE") String role
    ) {
        validateAdmin(role);
        return retailerProfileService.updateMyProfile(userId, dto);
    }

    /**
     * ADMIN: Update user status (ACTIVE / INACTIVE / SUSPENDED)
     */
    @PutMapping("/{userId}/status")
    public UserResponseDto updateUserStatus(
            @PathVariable Long userId,
            @RequestParam UserStatus status,
            @RequestHeader("X-USER-ROLE") String role
    ) {
        validateAdmin(role);
        return adminUserService.updateUserStatus(userId, status);
    }

    /* =========================
       ROLE VALIDATION
       ========================= */

    private void validateAdmin(String role) {
        if (!"ADMIN".equals(role)) {
            throw new RuntimeException("Access denied: ADMIN role required");
        }
    }
}
