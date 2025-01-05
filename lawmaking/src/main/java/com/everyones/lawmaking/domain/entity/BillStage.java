package com.everyones.lawmaking.domain.entity;

import lombok.Getter;

@Getter
public enum BillStage {
    RECEIVED(1, "접수"),
    COMMITTEE_REVIEW(2, "위원회 심사"),
    SYSTEMATIC_REVIEW(3, "체계자구 심사"),
    PLENARY_DISCUSSION(4, "본회의 심의"),
    GOVERNMENT_TRANSFER(5, "정부 이송"),
    PUBLICATION(6, "공포");



    private final String description;
    private final int order;


    BillStage(int order, String description) {
        this.order = order;
        this.description = description;
    }

    public static boolean canStageUpdate(BillStage current, BillStage next) {
        return current.getOrder() < next.getOrder();
    }

    public static BillStage fromDescription(String description) {
        for (BillStage stage : BillStage.values()) {
            if (stage.getDescription().equals(description)) {
                return stage;
            }
        }
        throw new IllegalArgumentException("No Found enum constant with description " + description);
    }
}
