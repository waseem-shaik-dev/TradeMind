package com.trademind.user.controller;

import com.trademind.user.service.AvatarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users/me/avatar")
@RequiredArgsConstructor
public class AvatarController {

    private final AvatarService avatarService;

    /**
     * Upload avatar (first time)
     */
    @PostMapping
    public ResponseEntity<String> uploadAvatar(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(
                avatarService.uploadAvatar(userId, file)
        );
    }

    /**
     * Update avatar (replace image)
     */
    @PutMapping
    public ResponseEntity<String> updateAvatar(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(
                avatarService.updateAvatar(userId, file)
        );
    }

    // URL UPLOAD
    @PostMapping("/url")
    public ResponseEntity<String> uploadAvatarByUrl(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestParam String imageUrl
    ) {
        return ResponseEntity.ok(
                avatarService.uploadAvatarByUrl(userId, imageUrl)
        );
    }

    // URL UPDATE
    @PutMapping("/url")
    public ResponseEntity<String> updateAvatarByUrl(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestParam String imageUrl
    ) {
        return ResponseEntity.ok(
                avatarService.updateAvatarByUrl(userId, imageUrl)
        );
    }

    /**
     * Delete avatar
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAvatar(
            @RequestHeader("X-USER-ID") Long userId
    ) {
        avatarService.deleteAvatar(userId);
        return ResponseEntity.noContent().build();
    }
}
