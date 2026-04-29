package com.trademind.user.service;

import com.trademind.user.dto.AdminProfileDto;

public interface AdminProfileService {
    AdminProfileDto getMyAdminProfile(Long userId);
    AdminProfileDto updateAdminProfile(Long userId, AdminProfileDto dto);
}
