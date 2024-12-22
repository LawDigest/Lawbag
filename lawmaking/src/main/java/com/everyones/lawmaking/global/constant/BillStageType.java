package com.everyones.lawmaking.global.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BillStageType {
    RECEIPT("접수", "접수"),
    STANDING_COMMITTEE_RECEIPT("위원회심사","소관위접수"),
    STANDING_COMMITTEE_AUDIT_BEFORE("위원회심사","위원회심사"),
    STANDING_COMMITTEE_AUDIT("위원회심사","소관위심사"),
    PLENARY_SESSION("본회의 심의","본회의 심의"),
    PROMULGATION("공포","공포");

    private String key;
    private String value;

    private static final Map<String, List<String>> KEY_MAP = new HashMap<>();

    static {
        for (BillStageType e : BillStageType.values()) {
            KEY_MAP.computeIfAbsent(e.key, k -> new ArrayList<>()).add(e.value);
        }
    }

    public static boolean containsValue(String stage) {
        return KEY_MAP.containsKey(stage);
    }

    public static List<String> getValueList(String stage) {
        return KEY_MAP.get(stage);
    }
}
