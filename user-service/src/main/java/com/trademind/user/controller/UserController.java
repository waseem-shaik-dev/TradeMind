package com.trademind.user.controller;

import com.trademind.user.dto.UserProfileDto;
import com.trademind.user.dto.UserResponseDto;
import com.trademind.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserResponseDto getMyProfile(
            @RequestHeader("X-USER-ID") Long userId) {
        return userService.getMyProfile(userId);
    }

    @PutMapping("/me/profile")
    public UserResponseDto updateMyProfile(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestBody UserProfileDto profileDto) {
        return userService.updateMyProfile(userId, profileDto);
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/{userId}/detailed")
    public ResponseEntity<UserResponseDto> getFullUser(
            @PathVariable Long userId
    ) {
        UserResponseDto response = userService.getFullUser(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/business")
    public ResponseEntity<UserResponseDto> getBusinessUser(
            @PathVariable Long userId
    ) {
        UserResponseDto response = userService.getBusinessUser(userId);
        return ResponseEntity.ok(response);
    }

}
