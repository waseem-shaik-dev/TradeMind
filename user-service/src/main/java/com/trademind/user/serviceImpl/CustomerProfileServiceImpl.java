package com.trademind.user.serviceImpl;

import com.trademind.user.dto.CustomerProfileDto;
import com.trademind.user.mapper.CustomerProfileMapper;
import com.trademind.user.repository.CustomerProfileRepository;
import com.trademind.user.service.CustomerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerProfileServiceImpl implements CustomerProfileService {
    private final CustomerProfileRepository customerProfileRepository;
    private final CustomerProfileMapper customerProfileMapper;

    @Override
    public CustomerProfileDto getMyCustomerProfile(Long userId) {
       return customerProfileRepository.findByUser_Id(userId)
               .map(customerProfileMapper::toDto)
               .orElseThrow(()->new RuntimeException("User with Id: "+userId+" not found"));
    }
}
