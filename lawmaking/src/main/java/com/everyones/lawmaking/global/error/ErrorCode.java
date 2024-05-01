package com.everyones.lawmaking.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    PARTY_NOT_FOUND(404, "해당 파티를 찾을 수 없습니다."),

    // 업데이트를 위해 제공된 파라미터와 DB 내부 값이 일치하여 반환해주는 에러
    BAD_UPDATE_PARAMETER(400, "업데이트 하려는 파라미터와 DB 내부 값이 일치합니다."),

    BAD_REQUEST(400, "잘못된 요청입니다."),

    INTERNAL_SERVER_ERROR(500, "서버 내부 오류입니다."),

    INVALID_QUERY_PARAMETER(400, "유효하지 않은 쿼리파라미터값이 포함되어있습니다");

    private final int code;
    private final String message;
}
