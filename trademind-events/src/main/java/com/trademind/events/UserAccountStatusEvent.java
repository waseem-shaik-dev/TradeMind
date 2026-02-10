package com.trademind.events;

public record UserAccountStatusEvent(
        Long userId,
        String action // SOFT_DELETE or RESTORE
) {}
