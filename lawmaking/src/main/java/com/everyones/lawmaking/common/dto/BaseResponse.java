package com.everyones.lawmaking.common.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BaseResponse {
    public static Map<String, Object> generateSuccessResponse(Object data) {
        return generateErrorResponse(true, 200, "", data);
    }

    public static Map<String, Object> generateSuccessResponse(Object data, long status) {
        return generateErrorResponse(true, status, "", data);
    }
    public static Map<String, Object> generateErrorResponse(
            boolean success, long status, String message, Object data) {
        var resp = new HashMap<String, Object>();
        resp.put("success", success);
        resp.put("status", status);
        resp.put("message", message);
        resp.put("data", data);
        return resp;
        // for test
    }
}