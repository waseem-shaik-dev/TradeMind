package com.trademind.user.serviceImpl;

import com.trademind.user.dto.UserResponseDto;
import com.trademind.user.entity.User;
import com.trademind.user.enums.UserRole;
import com.trademind.user.enums.UserStatus;
import com.trademind.user.mapper.UserMapper;
import com.trademind.user.repository.UserRepository;
import com.trademind.user.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public List<UserResponseDto> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserResponseDto updateUserStatus(Long userId, UserStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(status);
        return userMapper.toDto(userRepository.save(user));
    }
}
