package com.trademind.user.serviceImpl;

import com.trademind.user.entity.User;
import com.trademind.user.entity.UserProfile;
import com.trademind.user.repository.UserProfileRepository;
import com.trademind.user.repository.UserRepository;
import com.trademind.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final UserProfileRepository profileRepo;

    @Override
    public User getUser(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserProfile updateProfile(Long userId, UserProfile profile) {
        profile.setUserId(userId);
        return profileRepo.save(profile);
    }
}

