package com.everyones.lawmaking.global.config;

import com.everyones.lawmaking.domain.entity.ColumnEventType;
import com.everyones.lawmaking.domain.entity.Notification;
import com.everyones.lawmaking.domain.entity.User;
import com.everyones.lawmaking.facade.Facade;
import com.everyones.lawmaking.global.CustomException;
import com.everyones.lawmaking.global.ResponseCode;
import com.everyones.lawmaking.repository.NotificationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationCreator {

    private final NotificationRepository notificationRepository;
    private final Facade facade;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void createNotification(List<Map<String, List<String>>> filteredValuesByRows) {
        List<Notification> notifications = new ArrayList<>();
        filteredValuesByRows.forEach((eventDataByEventTypeMap) -> {
                    eventDataByEventTypeMap.forEach((eventType, eventData) -> {
                        try {
                            ColumnEventType columnEventType = ColumnEventType.from(eventType);
                            String jsonString = objectMapper.writeValueAsString(eventData);
                            List<User> subscribedSavedUser = facade.getSubscribedUsers(columnEventType, eventData.get(0));
                            subscribedSavedUser
                                    .forEach(user -> {
                                        notifications.add(
                                        Notification.builder()
                                                .notificationName(columnEventType)
                                                .contentJson(jsonString)
                                                .user(user)
                                                .build());
                                    });
                        } catch (JsonProcessingException e) {
                            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
                        }
                    });
                });
        notificationRepository.saveAll(notifications);
    }
}
