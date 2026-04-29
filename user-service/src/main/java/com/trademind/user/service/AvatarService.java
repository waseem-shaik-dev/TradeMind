package com.trademind.user.service;

import org.springframework.web.multipart.MultipartFile;

public interface AvatarService {

    String uploadAvatar(Long userId, MultipartFile file);

    String updateAvatar(Long userId, MultipartFile file);

    void deleteAvatar(Long userId);

    String uploadAvatarByUrl(Long userId, String imageUrl);

    String updateAvatarByUrl(Long userId, String imageUrl);
}
