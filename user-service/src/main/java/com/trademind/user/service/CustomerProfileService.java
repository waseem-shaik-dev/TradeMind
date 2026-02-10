package com.trademind.user.service;

import com.trademind.user.dto.CustomerProfileDto;

public interface CustomerProfileService {
    CustomerProfileDto getMyCustomerProfile(Long userId);
}
