package com.everyones.lawmaking.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    OK(HttpStatus.OK, 200, "요청이 정상적으로 처리되었습니다."),

    BAD_REQUEST(HttpStatus.BAD_REQUEST, 400, "잘못된 요청입니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "서버 내부 오류입니다."),

    INVALID_QUERY_PARAMETER(HttpStatus.BAD_REQUEST, 400, "유효하지 않은 쿼리파라미터값이 포함되어있습니다");

    private final HttpStatus status;

    private final int code;

    private final String message;


}
