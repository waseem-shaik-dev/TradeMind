package com.trademind.user.service;

import com.trademind.user.dto.RetailerProfileDto;

public interface RetailerProfileService {

    RetailerProfileDto getMyProfile(Long userId);

    RetailerProfileDto updateMyProfile(Long userId, RetailerProfileDto dto);

    RetailerProfileDto verifyRetailer(Long userId, boolean verified);
}
