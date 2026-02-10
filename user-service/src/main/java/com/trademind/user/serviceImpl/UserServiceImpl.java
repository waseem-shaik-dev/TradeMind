package com.trademind.user.serviceImpl;

import com.trademind.user.dto.UserProfileDto;
import com.trademind.user.dto.UserResponseDto;
import com.trademind.user.dto.internal.CartSourceInfoResponse;
import com.trademind.user.entity.MerchantProfile;
import com.trademind.user.entity.RetailerProfile;
import com.trademind.user.entity.User;
import com.trademind.user.entity.UserProfile;
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
    public UserResponseDto getUserById(Long userId) {
        return userMapper.toDto(getUser(userId));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
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
}
