package com.trademind.user.service;

import com.trademind.user.entity.User;
import com.trademind.user.entity.UserProfile;

public interface UserService {
    User getUser(Long id);
    UserProfile updateProfile(Long userId, UserProfile profile);
}
