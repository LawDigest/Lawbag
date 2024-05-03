package com.everyones.lawmaking.global.error;

import java.util.Map;

public class UserException extends CustomException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
    public UserException(ErrorCode errorCode, Map<String, String> inputValuesByProperty) {
        super(errorCode, inputValuesByProperty);
    }

    public static class UserNotFoundException extends UserException {
        public UserNotFoundException() {
            super(ErrorCode.USER_NOT_FOUND);
        }

        public UserNotFoundException(Map<String, String> inputValuesByProperty) {
            super(ErrorCode.USER_NOT_FOUND, inputValuesByProperty);
        }
    }


}
