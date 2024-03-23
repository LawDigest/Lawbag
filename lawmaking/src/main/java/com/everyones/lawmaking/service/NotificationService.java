package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.response.NotificationResponse;
import com.everyones.lawmaking.domain.entity.Notification;
import com.everyones.lawmaking.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    public List<NotificationResponse> getNotifications(long userId){
        List<Notification> notifications = notificationRepository.findAllByUserId(userId);

        return NotificationResponse.from(notifications);
    }



    public List<NotificationResponse> readNotifications(long userId){
        List<Notification> notifications = notificationRepository.findAllByUserId(userId);

        notifications.forEach((noti)-> noti.setRead(true));

        List<Notification> notificationsRead = notificationRepository.saveAll(notifications);

        return NotificationResponse.from(notificationsRead);
    }
}
