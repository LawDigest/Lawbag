package com.everyones.lawmaking.common.dto;

import com.everyones.lawmaking.domain.entity.Bill;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BillDetailInfo extends BillInfoDto{

    private String[] keyword;

    public BillDetailInfo(String billId, String billName, LocalDate proposeDate, String summary, String gptSummary, int viewCount, int billLikeCount, String stage, String keyword, String billStage, String briefSummary) {
        super(billId, billName, proposeDate, summary, gptSummary, viewCount, billLikeCount, billStage, briefSummary);
        this.keyword = keyword.split(", ");
    }

    public static BillDetailInfo from(Bill bill) {
        return new BillDetailInfo(bill.getId(), bill.getBillName(), bill.getProposeDate(), bill.getSummary(), bill.getGptSummary(), bill.getViewCount(), bill.getLikeCount(), bill.getStage(), bill.getKeyword(), bill.getStage(), bill.getBriefSummary());
    }

}

