package com.trademind.user.serviceImpl;

import com.trademind.user.dto.UserSummaryResponse;
import com.trademind.user.entity.User;
import com.trademind.user.entity.UserProfile;
import com.trademind.user.repository.UserProfileRepository;
import com.trademind.user.repository.UserRepository;
import com.trademind.user.service.UserSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSummaryServiceImpl implements UserSummaryService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    public UserSummaryResponse getMySummary(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User profile not found"));

        return new UserSummaryResponse(
                user.getId(),
                profile.getFullName(),
                profile.getAvatarUrl(),
                user.getRole().name()
        );
    }
}
