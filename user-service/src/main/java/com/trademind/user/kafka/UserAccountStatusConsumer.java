package com.trademind.user.kafka;

import com.trademind.events.UserAccountStatusEvent;
import com.trademind.user.entity.User;
import com.trademind.user.enums.UserStatus;
import com.trademind.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAccountStatusConsumer {

    private final UserRepository userRepository;

    @KafkaListener(
            topics = "USER_ACCOUNT_STATUS_TOPIC",
            groupId = "user-service"
    )
    @Transactional
    public void handle(UserAccountStatusEvent event) {

        User user = userRepository.findById(event.userId())
                .orElse(null);

        if (user == null) return;

        switch (event.action()) {

            case "SOFT_DELETE" -> user.setStatus(UserStatus.INACTIVE);

            case "RESTORE" -> user.setStatus(UserStatus.ACTIVE);
        }
    }
}
