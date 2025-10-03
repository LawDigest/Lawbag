package com.everyones.lawmaking.global.error;

import java.util.Map;

public class CommitteeCongressmanException extends CustomException{

    public CommitteeCongressmanException(ErrorCode errorCode) {
        super(errorCode);
    }
    public CommitteeCongressmanException(ErrorCode errorCode, Map<String, String> inputValueProperties) {
        super(errorCode, inputValueProperties);
    }

    public static class CommitteeCongressmanNotFound extends CommitteeCongressmanException {

        public CommitteeCongressmanNotFound() {
            super(ErrorCode.COMMITTEE_CONGRESSMAN_NOT_FOUND);
        }

        public CommitteeCongressmanNotFound(Map<String, String> inputValueProperties) {
            super(ErrorCode.COMMITTEE_CONGRESSMAN_NOT_FOUND, inputValueProperties);
        }

    }
}
