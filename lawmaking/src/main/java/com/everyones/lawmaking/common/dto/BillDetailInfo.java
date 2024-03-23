package com.everyones.lawmaking.common.dto;

import com.everyones.lawmaking.domain.entity.Bill;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BillDetailInfo extends BillInfoDto{

    private String stage;
    private String[] keyword;

    public BillDetailInfo(String billId, String billName, LocalDate proposeDate, String summary, String gptSummary, int viewCount, int billLikeCount, String stage, String keyword) {
        super(billId, billName, proposeDate, summary, gptSummary, viewCount, billLikeCount);
        this.stage = stage;
        this.keyword = keyword.split(", ");
    }

    public static BillDetailInfo from(Bill bill) {
        return new BillDetailInfo(bill.getId(), bill.getBillName(), bill.getProposeDate(), bill.getSummary(), bill.getGptSummary(), bill.getViewCount(), bill.getLikeCount(), bill.getStage(), bill.getKeyword());
    }

}

