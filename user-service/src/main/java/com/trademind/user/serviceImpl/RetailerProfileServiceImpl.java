package com.trademind.user.serviceImpl;

import com.trademind.user.dto.RetailerProfileDto;
import com.trademind.user.entity.RetailerProfile;
import com.trademind.user.mapper.RetailerProfileMapper;
import com.trademind.user.repository.RetailerProfileRepository;
import com.trademind.user.service.RetailerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RetailerProfileServiceImpl implements RetailerProfileService {

    private final RetailerProfileRepository retailerProfileRepository;
    private final RetailerProfileMapper retailerProfileMapper;

    @Override
    public RetailerProfileDto getMyProfile(Long userId) {
        RetailerProfile profile = getProfile(userId);
        return retailerProfileMapper.toDto(profile);
    }

    @Override
    public RetailerProfileDto updateMyProfile(Long userId, RetailerProfileDto dto) {
        RetailerProfile profile = getProfile(userId);

        profile.setShopName(dto.shopName());
        profile.setShopEmail(dto.shopEmail());
        profile.setGstNumber(dto.gstNumber());
        profile.setLatitude(dto.latitude());
        profile.setLongitude(dto.longitude());
        profile.setMapUrl(dto.mapUrl());

        return retailerProfileMapper.toDto(profile);
    }

    @Override
    public RetailerProfileDto verifyRetailer(Long userId, boolean verified) {
        RetailerProfile profile = getProfile(userId);
        profile.setVerified(verified);
        return retailerProfileMapper.toDto(profile);
    }

    private RetailerProfile getProfile(Long userId) {
        return retailerProfileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Retailer profile not found"));
    }

}
