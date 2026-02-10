package com.trademind.user.service;

import com.trademind.user.dto.UserResponseDto;
import com.trademind.user.enums.UserRole;
import com.trademind.user.enums.UserStatus;

import java.util.List;

public interface AdminUserService {

    List<UserResponseDto> getAllUsers();

    List<UserResponseDto> getUsersByRole(UserRole role);

    UserResponseDto updateUserStatus(Long userId, UserStatus status);
}
