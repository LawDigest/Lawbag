package com.everyones.lawmaking.global;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //custom error
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(final CustomException e) {
        e.printStackTrace();
        return ResponseEntity
                .status(e.getResponseCode().getStatus().value())
                .body(new ErrorResponse(e.getResponseCode()));
    }

    //500 error
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(final Exception e) {
        e.printStackTrace();
        return ResponseEntity
                .status(ResponseCode.INTERNAL_SERVER_ERROR.getStatus().value())
                .body(new ErrorResponse(ResponseCode.INTERNAL_SERVER_ERROR));
    }


}
