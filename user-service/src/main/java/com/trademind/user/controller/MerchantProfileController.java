package com.trademind.user.controller;

import com.trademind.user.dto.MerchantProfileDto;
import com.trademind.user.service.MerchantProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/merchant/profile")
@RequiredArgsConstructor
public class MerchantProfileController {

    private final MerchantProfileService merchantProfileService;

    /**
     * Merchant: Get own merchant profile
     */
    @GetMapping
    public MerchantProfileDto getMyProfile(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestHeader("X-USER-ROLE") String role
    ) {
        validateMerchant(role);
        return merchantProfileService.getMyProfile(userId);
    }

    /**
     * Merchant: Update own business details
     */
    @PutMapping
    public MerchantProfileDto updateMyProfile(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestHeader("X-USER-ROLE") String role,
            @RequestBody MerchantProfileDto dto
    ) {
        validateMerchant(role);
        return merchantProfileService.updateMyProfile(userId, dto);
    }

    /**
     * Admin: Verify / unverify merchant
     */
    @PutMapping("/{userId}/verify")
    public MerchantProfileDto verifyMerchant(
            @PathVariable Long userId,
            @RequestParam boolean verified,
            @RequestHeader("X-USER-ROLE") String role
    ) {
        validateAdmin(role);
        return merchantProfileService.verifyMerchant(userId, verified);
    }

    /* ======================
       ROLE VALIDATIONS
       ====================== */

    private void validateMerchant(String role) {
        if (!"MERCHANT".equals(role)) {
            throw new RuntimeException("Access denied: MERCHANT role required");
        }
    }

    private void validateAdmin(String role) {
        if (!"ADMIN".equals(role)) {
            throw new RuntimeException("Access denied: ADMIN role required");
        }
    }
}
