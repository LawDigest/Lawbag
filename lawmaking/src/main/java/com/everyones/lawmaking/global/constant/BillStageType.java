package com.everyones.lawmaking.global.constant;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BillStageType {
    RECEIPT("접수"),
    STANDING_COMMITTEE_RECEIPT("소관위접수"),
    // TODO: 위원회 심사와 소관위 심사가 같은 뜻이기 때문에, DB, 프론트가 적용해야함. 적용 후 위원회 심사 제거
    STANDING_COMMITTEE_AUDIT_TEMP("위원회심사"),
    STANDING_COMMITTEE_AUDIT("소관위심사"),
    EXAMINATION_OF_SYSTEM_AND_WORDING("체계자구 심사"),
    PLENARY_SESSION("본회의 심의"),
    REFERRAL_TO_THE_GOVERNMENT("정부이송"),
    PROMULGATION("공포"),
    DISCARDED_BY_INCORPORATING_ALTERNATIVES("대안반영폐기"),
    WITHDRAWAL("철회");

    private String value;

    private static final Set<String> VALUE_SET = new HashSet<>();

    static {
        for (BillStageType e : BillStageType.values()) {
            VALUE_SET.add(e.getValue());
        }
    }

    public static boolean containsValue(String stage) {
        return VALUE_SET.contains(stage);
    }
}
