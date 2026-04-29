package com.trademind.user.serviceImpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.trademind.user.entity.UserProfile;
import com.trademind.user.repository.UserProfileRepository;
import com.trademind.user.service.AvatarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AvatarServiceImpl implements AvatarService {

    private final Cloudinary cloudinary;
    private final UserProfileRepository userProfileRepository;

    @Override
    public String uploadAvatar(Long userId, MultipartFile file) {
        UserProfile profile = getProfile(userId);

        if (profile.getAvatarPublicId() != null) {
            throw new RuntimeException("Avatar already exists. Use update instead.");
        }

        Map uploadResult = uploadToCloudinary(file);

        profile.setAvatarUrl((String) uploadResult.get("secure_url"));
        profile.setAvatarPublicId((String) uploadResult.get("public_id"));

        return profile.getAvatarUrl();
    }

    @Override
    public String updateAvatar(Long userId, MultipartFile file) {
        UserProfile profile = getProfile(userId);

        // delete old avatar if exists
        if (profile.getAvatarPublicId() != null) {
            deleteFromCloudinary(profile.getAvatarPublicId());
        }

        Map uploadResult = uploadToCloudinary(file);

        profile.setAvatarUrl((String) uploadResult.get("secure_url"));
        profile.setAvatarPublicId((String) uploadResult.get("public_id"));

        return profile.getAvatarUrl();
    }

    @Override
    public void deleteAvatar(Long userId) {
        UserProfile profile = getProfile(userId);

        if (profile.getAvatarPublicId() != null) {
            deleteFromCloudinary(profile.getAvatarPublicId());
        }

        profile.setAvatarUrl(null);
        profile.setAvatarPublicId(null);
    }

    /* =====================
       INTERNAL HELPERS
       ===================== */

    private UserProfile getProfile(Long userId) {
        return userProfileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("User profile not found"));
    }

    private Map uploadToCloudinary(MultipartFile file) {
        try {
            return cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "trademind/avatars",
                            "resource_type", "image"
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload avatar", e);
        }
    }

    private void deleteFromCloudinary(String publicId) {
        try {
            cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.emptyMap()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete avatar", e);
        }
    }

    @Override
    public String uploadAvatarByUrl(Long userId, String imageUrl) {

        validateUrl(imageUrl);

        UserProfile profile = getProfile(userId);

        if (profile.getAvatarPublicId() != null || profile.getAvatarUrl() != null) {
            throw new RuntimeException("Avatar already exists. Use update instead.");
        }

        profile.setAvatarUrl(imageUrl);
        profile.setAvatarPublicId(null); // IMPORTANT

        return profile.getAvatarUrl();
    }

    @Override
    public String updateAvatarByUrl(Long userId, String imageUrl) {

        validateUrl(imageUrl);

        UserProfile profile = getProfile(userId);

        // delete old cloudinary image if exists
        if (profile.getAvatarPublicId() != null) {
            deleteFromCloudinary(profile.getAvatarPublicId());
        }

        profile.setAvatarUrl(imageUrl);
        profile.setAvatarPublicId(null);

        return profile.getAvatarUrl();
    }

    private void validateUrl(String url) {
        if (url == null || !url.startsWith("http")) {
            throw new RuntimeException("Invalid image URL");
        }
    }

}
