package com.trademind.user.mapper;

import com.trademind.user.dto.UserProfileDto;
import com.trademind.user.entity.User;
import com.trademind.user.entity.UserProfile;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper {

    public UserProfileDto toDto(UserProfile entity) {
        if (entity == null) return null;

        return new UserProfileDto(
                entity.getFullName(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getAvatarUrl()
        );
    }

    public UserProfile toEntity(UserProfileDto dto, User user) {
        if (dto == null) return null;

        return UserProfile.builder()
                .fullName(dto.fullName())
                .phone(dto.phone())
                .email(dto.email())
                .avatarUrl(dto.avatarUrl())
                .user(user)
                .build();
    }

    public void updateEntity(UserProfile entity, UserProfileDto dto) {
        if (entity == null || dto == null) return;

        entity.setFullName(dto.fullName());
        entity.setPhone(dto.phone());
        entity.setEmail(dto.email());

    }
}
