package com.everyones.lawmaking.common.dto;

import com.everyones.lawmaking.domain.entity.Bill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BillInfoDto {

    private String billId;
    private String billName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate proposeDate;
    private String summary;
    private String gptSummary;
    private int viewCount;
    private int billLikeCount;
    private String billStage;
    private String briefSummary;


    public BillInfoDto(Bill bill) {
        this.billId = bill.getId();
        this.billName = bill.getBillName();
        this.proposeDate = bill.getProposeDate();
        this.summary = bill.getSummary();
        this.gptSummary = bill.getGptSummary();
        this.viewCount = bill.getViewCount();
        this.billLikeCount = bill.getLikeCount();
        this.billStage = bill.getStage();
        this.briefSummary = bill.getBriefSummary();
    }

    public static BillInfoDto from(Bill bill) {
        return BillInfoDto.builder()
                .billId(bill.getId())
                .billName(bill.getBillName())
                .proposeDate(bill.getProposeDate())
                .summary(bill.getSummary())
                .gptSummary(bill.getGptSummary())
                .viewCount(bill.getViewCount())
                .billLikeCount(bill.getLikeCount())
                .billStage(bill.getStage())
                .briefSummary(bill.getBriefSummary())
                .build();
    }

}
