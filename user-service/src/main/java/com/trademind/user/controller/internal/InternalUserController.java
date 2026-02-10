package com.trademind.user.controller.internal;

import com.trademind.user.dto.internal.CartSourceInfoResponse;
import com.trademind.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/user")
@RequiredArgsConstructor
public class InternalUserController {

    private final UserService userService;

    // Used by Cart Service
    @GetMapping("/cart-source")
    public CartSourceInfoResponse getCartSourceInfo(
            @RequestParam Long sourceId,
            @RequestParam String sourceType) {

        return userService.getCartSourceInfo(sourceId, sourceType);
    }
}
