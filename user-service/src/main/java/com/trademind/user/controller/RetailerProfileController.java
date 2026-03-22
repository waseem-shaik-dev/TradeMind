package com.trademind.user.controller;

import com.trademind.user.dto.RetailerProfileDto;
import com.trademind.user.service.RetailerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/retailer/profile")
@RequiredArgsConstructor
public class RetailerProfileController {

    private final RetailerProfileService retailerProfileService;

    /**
     * Retailer: Get own retailer profile
     */
    @GetMapping
    public RetailerProfileDto getMyProfile(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestHeader("X-USER-ROLE") String role
    ) {
        validateRetailer(role);
        return retailerProfileService.getMyProfile(userId);
    }

    /**
     * Retailer: Update own shop details
     */
    @PutMapping
    public RetailerProfileDto updateMyProfile(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestHeader("X-USER-ROLE") String role,
            @RequestBody RetailerProfileDto dto
    ) {
        validateRetailer(role);
        return retailerProfileService.updateMyProfile(userId, dto);
    }

    /**
     * Admin: Verify / unverify retailer
     */
//    @PutMapping("/{userId}/verify")
//    public RetailerProfileDto verifyRetailer(
//            @PathVariable Long userId,
//            @RequestParam boolean verified,
//            @RequestHeader("X-USER-ROLE") String role
//    ) {
//        validateAdmin(role);
//        return retailerProfileService.verifyRetailer(userId, verified);
//    }

    /* ======================
       ROLE VALIDATIONS
       ====================== */

    private void validateRetailer(String role) {
        if (!"RETAILER".equals(role)) {
            throw new RuntimeException("Access denied: RETAILER role required");
        }
    }

    private void validateAdmin(String role) {
        if (!"ADMIN".equals(role)) {
            throw new RuntimeException("Access denied: ADMIN role required");
        }
    }
}
