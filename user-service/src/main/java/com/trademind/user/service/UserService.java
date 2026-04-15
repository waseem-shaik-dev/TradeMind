package com.trademind.user.service;

import com.trademind.events.common.SellerSnapshotDto;
import com.trademind.user.dto.UserCountResponse;
import com.trademind.user.dto.UserGrowthResponse;
import com.trademind.user.dto.UserResponseDto;
import com.trademind.user.dto.UserProfileDto;
import com.trademind.user.dto.internal.CartSourceInfoResponse;

public interface UserService {

    UserResponseDto getMyProfile(Long userId);

    UserResponseDto updateMyProfile(Long userId, UserProfileDto profileDto);

    UserResponseDto getUserById(Long userId);

    CartSourceInfoResponse getCartSourceInfo(Long sourceId, String sourceType);

    UserResponseDto getFullUser(Long userId);

    UserResponseDto getBusinessUser(Long userId);

    SellerSnapshotDto getSellerSnapshot(Long sourceId, String sourceType);

}
