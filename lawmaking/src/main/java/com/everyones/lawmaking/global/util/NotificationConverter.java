package com.everyones.lawmaking.global.util;

import com.everyones.lawmaking.common.dto.response.NotificationResponse;
import com.everyones.lawmaking.domain.entity.ColumnEventType;
import com.everyones.lawmaking.domain.entity.Notification;
import com.everyones.lawmaking.facade.Facade;
import com.everyones.lawmaking.global.CustomException;
import com.everyones.lawmaking.global.ResponseCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class NotificationConverter {

    // NotificationResponse를 만드는 메소드가 static 메소드 이기에 안에 들어가는 객체들도 전부 static이어야 함.
    // static을 하지 않으면 사용하는 객체를 생성할 때 facade를 계속 의존성 주입 해줘야함
    // 그런데 NotificationResponse Dto 클래스에 Facade 의존성 주입을 해주는 것이 클래스 목적성을 흐린다고 생각하여 해당 클래스를 만듦.
    // 따라서 처음에 facade를 static으로 선언 된 것을 필드 주입을 통해서 facade 인스턴스를 초기화
    private static Facade facade;

    @Autowired
    private void setFacade(Facade facade) {
        NotificationConverter.facade = facade;
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    private static List<String> parseData(String data) {
        try {
            return mapper.readValue(data, new TypeReference<List<String>>() {
            });
        } catch (JsonProcessingException e) {
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    public static NotificationResponse from(Notification notification) {
        ColumnEventType columnEventType = notification.getNotificationName();
        List<String> data = parseData(notification.getContentJson());
        String type = columnEventType.getEventName();
        String target = data.get(0);
        String title = null;
        String content = null;
        List<String> processedData;
        switch (columnEventType) {
            case RP_INSERT:
                processedData = facade.rpInsert(data);
                // List.of(billId, billRepProposer, billProposers, billName)
                title = processedData.get(1) + " 의원";
                content = processedData.get(2) + "이 '" + processedData.get(3) + "'을/를 발의했어요!";
                break;
            case BILL_STAGE_UPDATE:
                // List.of("bill_id", "bill_name", "proposers", "stage")
                processedData = facade.billStageUpdate(data);
                title = processedData.get(1) + " " + processedData.get(2);
                content = "스크랩한 법안이 본회의에서 '"+processedData.get(3)+"'되었어요!";
                break;
            case CONGRESSMAN_PARTY_UPDATE:
                // List.of(congressmanId, partyName, congressmanName);
                processedData = facade.congressmanPartyUpdate(data);
                title = processedData.get(1);
                content = "'"+processedData.get(2)+"'의원의 당적이 '"+processedData.get(1)+"'(으)로 변동했어요!";
                break;
        }

        LocalDateTime createdDate = notification.getCreatedDate();

        if (type == null || target == null || title == null || content == null || createdDate.isAfter(LocalDateTime.now())) {
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }


        return NotificationResponse.builder()
                // 이벤트 타입
                .type(type)
                // 알림 이미지 id값
                .target(target)
                .title(title)
                .content(content)
                .createdDate(createdDate)
                .build();
    }

}
