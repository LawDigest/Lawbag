package com.everyones.lawmaking.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    OK(HttpStatus.OK, 200, "요청이 정상적으로 처리되었습니다."),

    BAD_REQUEST(HttpStatus.BAD_REQUEST, 400, "잘못된 요청입니다.");

    private final HttpStatus status;

    private final int code;

    private final String message;


}
