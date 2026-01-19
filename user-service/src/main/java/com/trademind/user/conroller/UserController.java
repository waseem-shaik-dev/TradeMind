package com.trademind.user.conroller;

import com.trademind.user.entity.User;
import com.trademind.user.entity.UserProfile;
import com.trademind.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PutMapping("/{id}/profile")
    public UserProfile updateProfile(
            @PathVariable Long id,
            @RequestBody UserProfile profile) {
        return userService.updateProfile(id, profile);
    }
}
