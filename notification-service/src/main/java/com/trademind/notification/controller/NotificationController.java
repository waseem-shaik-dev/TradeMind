package com.trademind.notification.controller;

import com.trademind.notification.enums.NotificationType;
import com.trademind.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @PostMapping("/test")
    public String sendTest(@RequestParam String email) {
        service.sendNotification(
                email,
                NotificationType.ORDER_PLACED,
                "This is a test email from TradeMind"
        );
        return "Email sent";
    }
}
