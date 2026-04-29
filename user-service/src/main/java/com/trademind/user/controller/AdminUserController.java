package com.trademind.user.controller;

import com.trademind.user.dto.*;
import com.trademind.user.enums.UserRole;
import com.trademind.user.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;
    private final AvatarService avatarService;
    private final MerchantProfileService merchantService;
    private final RetailerProfileService retailerService;
    private final AdminProfileService adminProfileService;
    private final ShopImageService shopImageService;
    private final StoreAddressService storeAddressService;
    private final AdminUserService adminUserService;


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
       👤 USER PROFILE TAB
       ========================= */

    @PutMapping("/{userId}/profile")
    public UserResponseDto updateUserProfile(
            @PathVariable Long userId,
            @RequestBody UserProfileDto dto,
            @RequestHeader("X-USER-ROLE") String role
    ) {
        validateAdmin(role);
        return userService.updateMyProfile(userId, dto);
    }

    /* -------- AVATAR -------- */

    @PutMapping("/{userId}/avatar")
    public ResponseEntity<String> updateAvatar(
            @PathVariable Long userId,
            @RequestParam MultipartFile file,
            @RequestHeader("X-USER-ROLE") String role
    ) {
        validateAdmin(role);
        return ResponseEntity.ok(avatarService.updateAvatar(userId, file));
    }

    @PutMapping("/{userId}/avatar/url")
    public ResponseEntity<String> updateAvatarByUrl(
            @PathVariable Long userId,
            @RequestParam String imageUrl,
            @RequestHeader("X-USER-ROLE") String role
    ) {
        validateAdmin(role);
        return ResponseEntity.ok(avatarService.updateAvatarByUrl(userId, imageUrl));
    }

    /* =========================
       🏪 MERCHANT TAB
       ========================= */

    @PutMapping("/{userId}/merchant-profile")
    public MerchantProfileDto updateMerchantProfile(
            @PathVariable Long userId,
            @RequestBody MerchantProfileDto dto,
            @RequestHeader("X-USER-ROLE") String role
    ) {
        validateAdmin(role);
        return merchantService.updateMyProfile(userId, dto);
    }

    @PutMapping("/{userId}/merchant-profile/shop-image")
    public String updateMerchantShopImage(
            @PathVariable Long userId,
            @RequestParam MultipartFile file,
            @RequestHeader("X-USER-ROLE") String role
    ) {
        validateAdmin(role);
        return shopImageService.updateMerchantShopImage(userId, file);
    }

    @PutMapping("/{userId}/merchant-profile/shop-image/url")
    public String updateMerchantShopImageByUrl(
            @PathVariable Long userId,
            @RequestParam String imageUrl,
            @RequestHeader("X-USER-ROLE") String role
    ) {
        validateAdmin(role);
        return shopImageService.updateMerchantShopImageByUrl(userId, imageUrl);
    }

    /* =========================
       🏪 RETAILER TAB
       ========================= */

    @PutMapping("/{userId}/retailer-profile")
    public RetailerProfileDto updateRetailerProfile(
            @PathVariable Long userId,
            @RequestBody RetailerProfileDto dto,
            @RequestHeader("X-USER-ROLE") String role
    ) {
        validateAdmin(role);
        return retailerService.updateMyProfile(userId, dto);
    }

    @PutMapping("/{userId}/retailer-profile/shop-image")
    public String updateRetailerShopImage(
            @PathVariable Long userId,
            @RequestParam MultipartFile file,
            @RequestHeader("X-USER-ROLE") String role
    ) {
        validateAdmin(role);
        return shopImageService.updateRetailerShopImage(userId, file);
    }

    @PutMapping("/{userId}/retailer-profile/shop-image/url")
    public String updateRetailerShopImageByUrl(
            @PathVariable Long userId,
            @RequestParam String imageUrl,
            @RequestHeader("X-USER-ROLE") String role
    ) {
        validateAdmin(role);
        return shopImageService.updateRetailerShopImageByUrl(userId, imageUrl);
    }

    /* =========================
       👑 ADMIN PROFILE TAB
       ========================= */

    @PutMapping("/{userId}/admin-profile")
    public AdminProfileDto updateAdminProfile(
            @PathVariable Long userId,
            @RequestBody AdminProfileDto dto,
            @RequestHeader("X-USER-ROLE") String role
    ) {
        validateAdmin(role);
        return adminProfileService.updateAdminProfile(userId, dto);
    }

    /* ========================= */

    private void validateAdmin(String role) {
        if (!"ADMIN".equals(role)) {
            throw new RuntimeException("Access denied: ADMIN role required");
        }
    }

    /* =========================
   🏪 MERCHANT STORE ADDRESS
   ========================= */

    @PutMapping("/{userId}/merchant-profile/store-address")
    public StoreAddressDto updateMerchantStoreAddress(
            @PathVariable Long userId,
            @RequestBody StoreAddressDto dto,
            @RequestHeader("X-USER-ROLE") String role
    ) {
        validateAdmin(role);
        return storeAddressService.updateStoreAddressByAdmin(userId, dto);
    }


/* =========================
   🏪 RETAILER STORE ADDRESS
   ========================= */

    @PutMapping("/{userId}/retailer-profile/store-address")
    public StoreAddressDto updateRetailerStoreAddress(
            @PathVariable Long userId,
            @RequestBody StoreAddressDto dto,
            @RequestHeader("X-USER-ROLE") String role
    ) {
        validateAdmin(role);
        return storeAddressService.updateStoreAddressByAdmin(userId, dto);
    }
}