package com.trademind.user.mapper;

import com.trademind.user.dto.AdminProfileDto;
import com.trademind.user.entity.AdminProfile;
import com.trademind.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AdminProfileMapper {

    public AdminProfileDto toDto(AdminProfile entity) {
        if (entity == null) return null;

        return new AdminProfileDto(
                entity.getDepartment(),
                entity.getDesignation()
        );
    }

    public AdminProfile toEntity(AdminProfileDto dto, User user) {
        if (dto == null) return null;

        return AdminProfile.builder()
                .user(user)
                .department(dto.department())
                .designation(dto.designation())
                .build();
    }
}
