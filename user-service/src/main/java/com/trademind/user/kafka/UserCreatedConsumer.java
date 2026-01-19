package com.trademind.user.kafka;

import com.trademind.user.entity.User;
import com.trademind.user.enums.UserRole;
import com.trademind.user.enums.UserStatus;
import com.trademind.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserCreatedConsumer {

    private final UserRepository userRepository;

    @KafkaListener(topics = "USER_CREATED_TOPIC", groupId = "user-service")
    public void consumeUserCreated(String userId) {

        Long id = Long.parseLong(userId);

        User user = User.builder()
                .id(id)
                .role(UserRole.CUSTOMER) // default
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }
}

