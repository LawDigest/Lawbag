package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.domain.entity.Notification;
import com.everyones.lawmaking.global.util.NotificationConverter;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NotificationResponse {

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private String target;

    @NotNull
    private String type;

    @NotNull
    private LocalDateTime createdDate;

    // notifications 리스트를 NotificationResponse 리스트로 만들어주는 메서드
    public static List<NotificationResponse> from(List<Notification> notifications) {
        return notifications.stream().map(NotificationConverter::from).toList();
    }

}
