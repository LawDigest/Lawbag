package com.everyones.lawmaking.global.error;

import java.util.Map;

public class CandidateException extends CustomException {

    public CandidateException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CandidateException(ErrorCode errorCode, Map<String, String> inputValuesByProperty) {
        super(errorCode, inputValuesByProperty);
    }

    public static class CandidateNotFound extends CandidateException {
        public CandidateNotFound() {
            super(ErrorCode.CANDIDATE_NOT_FOUND);
        }
        public CandidateNotFound(Map<String, String> inputValuesByProperty) {
            super(ErrorCode.CANDIDATE_NOT_FOUND, inputValuesByProperty);
        }
    }
}
