package com.trademind.user.mapper;

import com.trademind.user.dto.MerchantProfileDto;
import com.trademind.user.entity.MerchantProfile;
import com.trademind.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class MerchantProfileMapper {

    public MerchantProfileDto toDto(MerchantProfile entity) {
        if (entity == null) return null;

        return new MerchantProfileDto(
                entity.getBusinessName(),
                entity.getBusinessEmail()
        );
    }

    public MerchantProfile toEntity(MerchantProfileDto dto, User user) {
        if (dto == null) return null;

        return MerchantProfile.builder()
                .user(user)
                .businessName(dto.businessName())
                .businessEmail(dto.businessEmail())
                .build();
    }
}
