package com.trademind.user.service;

import com.trademind.user.dto.UserResponseDto;
import com.trademind.user.dto.UserProfileDto;
import com.trademind.user.dto.internal.CartSourceInfoResponse;

public interface UserService {

    UserResponseDto getMyProfile(Long userId);

    UserResponseDto updateMyProfile(Long userId, UserProfileDto profileDto);

    UserResponseDto getUserById(Long userId);

    CartSourceInfoResponse getCartSourceInfo(Long sourceId, String sourceType);

}
