package com.trademind.user.serviceImpl;

import com.trademind.user.dto.AdminProfileDto;
import com.trademind.user.entity.AdminProfile;
import com.trademind.user.mapper.AdminProfileMapper;
import com.trademind.user.repository.AdminProfileRepository;
import com.trademind.user.service.AdminProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminProfileServiceImpl implements AdminProfileService {

    private final AdminProfileRepository repository;
    private final AdminProfileMapper mapper;

    @Override
    public AdminProfileDto getMyAdminProfile(Long userId) {
        return mapper.toDto(
                repository.findByUser_Id(userId)
                        .orElseThrow(() -> new RuntimeException("Admin profile not found"))
        );
    }

    @Override
    public AdminProfileDto updateAdminProfile(Long userId, AdminProfileDto dto) {

        AdminProfile profile = repository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Admin profile not found"));

        profile.setDepartment(dto.department());
        profile.setDesignation(dto.designation());

        return mapper.toDto(profile);
    }

}
