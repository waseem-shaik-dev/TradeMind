package com.trademind.user.serviceImpl;

import com.trademind.events.common.SellerSnapshotDto;
import com.trademind.events.common.StoreAddressSnapshotDto;
import com.trademind.user.dto.UserCountResponse;
import com.trademind.user.dto.UserGrowthResponse;
import com.trademind.user.dto.UserProfileDto;
import com.trademind.user.dto.UserResponseDto;
import com.trademind.user.dto.internal.CartSourceInfoResponse;
import com.trademind.user.entity.*;
import com.trademind.user.enums.UserRole;
import com.trademind.user.mapper.UserMapper;
import com.trademind.user.mapper.UserProfileMapper;
import com.trademind.user.repository.MerchantProfileRepository;
import com.trademind.user.repository.RetailerProfileRepository;
import com.trademind.user.repository.UserProfileRepository;
import com.trademind.user.repository.UserRepository;
import com.trademind.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final MerchantProfileRepository merchantProfileRepository;
    private final RetailerProfileRepository retailerProfileRepository;

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getMyProfile(Long userId) {
        User user = getUser(userId);
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto updateMyProfile(Long userId, UserProfileDto profileDto) {
        User user = getUser(userId);
        userProfileMapper.updateEntity(user.getProfile(), profileDto);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long userId) {
        return userMapper.toDto(getUser(userId));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public CartSourceInfoResponse getCartSourceInfo(
            Long sourceId,
            String sourceType) {

        UserRole role = UserRole.valueOf(sourceType);

        UserProfile userProfile = userProfileRepository.findByUserId(sourceId).orElseThrow(
                () ->    new RuntimeException("user profile not found ")
        );

        return switch (role) {

            case MERCHANT -> {
                MerchantProfile merchant =
                        merchantProfileRepository.findByUser_Id(sourceId)
                                .orElseThrow(() ->
                                        new RuntimeException("Merchant profile not found"));

                yield new CartSourceInfoResponse(
                        sourceId,
                        sourceType,
                        merchant.getBusinessName(),
                        userProfile.getAvatarUrl()
                );
            }

            case RETAILER -> {
                RetailerProfile retailer =
                        retailerProfileRepository.findByUser_Id(sourceId)
                                .orElseThrow(() ->
                                        new RuntimeException("Retailer profile not found"));

                yield new CartSourceInfoResponse(
                        sourceId,
                        sourceType,
                        retailer.getShopName(),
                        userProfile.getAvatarUrl()
                );
            }

            default -> throw new RuntimeException("Unsupported source type");
        };
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getFullUser(Long userId) {
        User user = userRepository.findCompleteUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getBusinessUser(Long userId) {
        User user = userRepository.findCompleteUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toBusinessDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public SellerSnapshotDto getSellerSnapshot(Long sourceId, String sourceType) {

        User user = userRepository.findCompleteUserById(sourceId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (sourceType.equals("MERCHANT") && user.getMerchantProfile() != null) {

            var m = user.getMerchantProfile();

            return new SellerSnapshotDto(
                    user.getId(),
                    "MERCHANT",
                    m.getBusinessName(),
                    m.getBusinessEmail(),
                    m.getShopImageUrl(),
                    mapStore(m.getStoreAddress())
            );
        }

        if (sourceType.equals("RETAILER") && user.getRetailerProfile() != null) {

            var r = user.getRetailerProfile();

            return new SellerSnapshotDto(
                    user.getId(),
                    "RETAILER",
                    r.getShopName(),
                    r.getShopEmail(),
                    r.getShopImageUrl(),
                    mapStore(r.getStoreAddress())
            );
        }

        throw new RuntimeException("Invalid seller");
    }

    private StoreAddressSnapshotDto mapStore(StoreAddress sa) {
        if (sa == null) return null;

        return new StoreAddressSnapshotDto(
                sa.getLine1(),
                sa.getLine2(),
                sa.getCity(),
                sa.getState(),
                sa.getPincode(),
                sa.getCountry(),
                sa.getLatitude(),
                sa.getLongitude(),
                sa.getMapUrl()
        );
    }

}
