package com.everyones.lawmaking.global.config;

import com.everyones.lawmaking.domain.entity.ColumnEventType;
import com.everyones.lawmaking.domain.entity.Notification;
import com.everyones.lawmaking.domain.entity.User;
import com.everyones.lawmaking.facade.Facade;
import com.everyones.lawmaking.global.error.CommonException;
import com.everyones.lawmaking.repository.NotificationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationCreator {

    private final NotificationRepository notificationRepository;
    private final Facade facade;
    private final ObjectMapper objectMapper = new ObjectMapper();

    //ToDo: 알림 데이터 삽입 시 유니크키 컬럼을 추가해서 다중 스프링서버환경에서도 중복 데이터가 삽입되지 않도록 처리
    @Transactional
    public void createNotification(List<Map<String, List<String>>> filteredValuesByRows) {
        List<Notification> notifications = new ArrayList<>();
        filteredValuesByRows.forEach((eventDataByEventTypeMap) -> {
                    eventDataByEventTypeMap.forEach((eventType, eventData) -> {
                        try {
                            // eventData의 첫번째 값은 항상 targetId
                            ColumnEventType columnEventType = ColumnEventType.from(eventType);
                            List<User> subscribedSavedUser = facade.getSubscribedUsers(columnEventType, eventData.get(0));
                            List<String> processedData = facade.getProcessedData(columnEventType,eventData);
                            if (processedData.isEmpty()) {
                                return;
                            }
                            String jsonString = objectMapper.writeValueAsString(processedData);

                            subscribedSavedUser
                                    .forEach(user -> {
                                        notifications.add(
                                        Notification.builder()
                                                .notificationName(columnEventType.name())
                                                .contentJson(jsonString)
                                                .user(user)
                                                .build());
                                    });
                        } catch (JsonProcessingException e) {
                            throw new CommonException.JsonParsingException();
                        }
                    });
                });
        notificationRepository.saveAll(notifications);
    }
}
