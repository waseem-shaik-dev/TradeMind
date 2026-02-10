package com.trademind.user.dto;

import com.trademind.user.enums.UserRole;
import com.trademind.user.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponseDto(
        Long id,
        UserRole role,
        UserStatus status,
        LocalDateTime createdAt,
        UserProfileDto profile,
        List<AddressDto> addresses,
        AdminProfileDto adminProfile,
        CustomerProfileDto customerProfile,
        MerchantProfileDto merchantProfile,
        RetailerProfileDto retailerProfile
) {}
