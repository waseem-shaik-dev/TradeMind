package com.trademind.user.service;


import com.trademind.user.dto.MerchantProfileDto;

public interface MerchantProfileService {

    MerchantProfileDto getMyProfile(Long userId);

    MerchantProfileDto updateMyProfile(Long userId, MerchantProfileDto dto);

  //  MerchantProfileDto verifyMerchant(Long userId, boolean verified);
}
