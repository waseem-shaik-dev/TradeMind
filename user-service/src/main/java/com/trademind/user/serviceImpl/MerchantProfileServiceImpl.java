package com.trademind.user.serviceImpl;

import com.trademind.user.dto.MerchantProfileDto;
import com.trademind.user.entity.MerchantProfile;
import com.trademind.user.mapper.MerchantProfileMapper;
import com.trademind.user.repository.MerchantProfileRepository;
import com.trademind.user.service.MerchantProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MerchantProfileServiceImpl implements MerchantProfileService {

    private final MerchantProfileRepository merchantProfileRepository;
    private final MerchantProfileMapper merchantProfileMapper;

    @Override
    public MerchantProfileDto getMyProfile(Long userId) {
        MerchantProfile profile = getProfile(userId);
        return merchantProfileMapper.toDto(profile);
    }

    @Override
    public MerchantProfileDto updateMyProfile(Long userId, MerchantProfileDto dto) {
        MerchantProfile profile = getProfile(userId);

        profile.setBusinessName(dto.businessName());
        profile.setBusinessEmail(dto.businessEmail());

        return merchantProfileMapper.toDto(profile);
    }



    private MerchantProfile getProfile(Long userId) {
        return merchantProfileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Merchant profile not found"));
    }

}
