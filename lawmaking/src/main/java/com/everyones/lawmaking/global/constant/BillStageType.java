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
    RECEIPT(1,"접수", "접수"),
    STANDING_COMMITTEE_RECEIPT(2,"위원회 심사","소관위접수"),
    STANDING_COMMITTEE_AUDIT_BEFORE(3,"위원회 심사","위원회 심사"),
    SYSTEMATIC_REVIEW(4,"체계자구 심사", "체계자구 심사"),
    PLENARY_SESSION(5,"본회의 심의","본회의 심의"),
    GOVERNMENT_TRANSFER(6, "정부이송","정부이송"),
    PROMULGATION(7,"공포","공포");

    private int order;
    private String key;
    private String value;

    private static final Map<String, List<String>> KEY_MAP = new HashMap<>();

    static {
        for (BillStageType e : BillStageType.values()) {
            KEY_MAP.computeIfAbsent(e.key, k -> new ArrayList<>()).add(e.value);
        }
    }

    public static boolean canUpdateStage(BillStageType current, BillStageType next) {
        return current.getOrder() < next.getOrder();
    }

    public static BillStageType fromValue(String value) {
        for (BillStageType stage : BillStageType.values()) {
            if (stage.getValue().equals(value)) {
                return stage;
            }
        }
        throw new IllegalArgumentException("No Found enum constant with value " + value);
    }

    public static boolean containsValue(String stage) {
        return KEY_MAP.containsKey(stage);
    }

    public static List<String> getValueList(String stage) {
        return KEY_MAP.get(stage);
    }
}
