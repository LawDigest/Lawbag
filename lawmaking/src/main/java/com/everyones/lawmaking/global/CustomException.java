package com.everyones.lawmaking.global;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {
    private final ResponseCode responseCode;
}
