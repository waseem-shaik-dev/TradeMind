package com.trademind.user.controller;

import com.trademind.user.dto.UserSummaryResponse;
import com.trademind.user.service.UserSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
public class UserSummaryController {

    private final UserSummaryService userSummaryService;

    @GetMapping("/summary")
    public UserSummaryResponse getMySummary(
            @RequestHeader("X-USER-ID") Long userId
    ) {
        return userSummaryService.getMySummary(userId);
    }
}
