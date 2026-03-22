package com.trademind.user.serviceImpl;

import com.trademind.user.dto.StoreAddressDto;
import com.trademind.user.entity.MerchantProfile;
import com.trademind.user.entity.RetailerProfile;
import com.trademind.user.entity.StoreAddress;
import com.trademind.user.entity.User;
import com.trademind.user.mapper.StoreAddressMapper;
import com.trademind.user.repository.MerchantProfileRepository;
import com.trademind.user.repository.RetailerProfileRepository;
import com.trademind.user.repository.StoreAddressRepository;
import com.trademind.user.repository.UserRepository;
import com.trademind.user.service.StoreAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreAddressServiceImpl implements StoreAddressService {

    private final StoreAddressRepository storeAddressRepository;
    private final MerchantProfileRepository merchantProfileRepository;
    private final RetailerProfileRepository retailerProfileRepository;
    private final UserRepository userRepository;
    private final StoreAddressMapper mapper;

    @Override
    public StoreAddressDto getStoreAddress(Long userId) {

        User user = getUser(userId);

        StoreAddress address = getStoreAddressFromProfile(user);

        return mapper.toDto(address);
    }

    @Override
    public StoreAddressDto createStoreAddress(Long userId, StoreAddressDto dto) {

        User user = getUser(userId);

        StoreAddress existing = getStoreAddressFromProfile(user);

        if (existing != null) {
            throw new RuntimeException("Store address already exists. Use update.");
        }

        StoreAddress address = mapper.toEntity(dto);

        storeAddressRepository.save(address);

        attachToProfile(user, address);

        return mapper.toDto(address);
    }

    @Override
    public StoreAddressDto updateStoreAddress(Long userId, StoreAddressDto dto) {

        User user = getUser(userId);

        StoreAddress address = getStoreAddressFromProfile(user);

        if (address == null) {
            throw new RuntimeException("Store address not found");
        }

        address.setLine1(dto.line1());
        address.setLine2(dto.line2());
        address.setCity(dto.city());
        address.setState(dto.state());
        address.setPincode(dto.pincode());
        address.setCountry(dto.country());
        address.setLatitude(dto.latitude());
        address.setLongitude(dto.longitude());
        address.setMapUrl(dto.mapUrl());

        return mapper.toDto(address);
    }

    @Override
    public void deleteStoreAddress(Long userId) {

        User user = getUser(userId);

        StoreAddress address = getStoreAddressFromProfile(user);

        if (address == null) {
            return;
        }

        detachFromProfile(user);

        storeAddressRepository.delete(address);
    }

    /* ==========================================
       INTERNAL HELPERS
       ========================================== */

    private User getUser(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private StoreAddress getStoreAddressFromProfile(User user) {

        if (user.getRetailerProfile() != null) {
            return user.getRetailerProfile().getStoreAddress();
        }

        if (user.getMerchantProfile() != null) {
            return user.getMerchantProfile().getStoreAddress();
        }

        throw new RuntimeException("User is not merchant or retailer");
    }

    private void attachToProfile(User user, StoreAddress address) {

        if (user.getRetailerProfile() != null) {

            RetailerProfile retailer = user.getRetailerProfile();

            retailer.setStoreAddress(address);

        } else if (user.getMerchantProfile() != null) {

            MerchantProfile merchant = user.getMerchantProfile();

            merchant.setStoreAddress(address);

        } else {
            throw new RuntimeException("User is not merchant or retailer");
        }
    }

    private void detachFromProfile(User user) {

        if (user.getRetailerProfile() != null) {

            user.getRetailerProfile().setStoreAddress(null);

        } else if (user.getMerchantProfile() != null) {

            user.getMerchantProfile().setStoreAddress(null);

        }
    }
}