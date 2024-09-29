package com.everyones.lawmaking.common.dto;

import com.everyones.lawmaking.domain.entity.Bill;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BillOutlineDto {
    private PartyDto partyInfo;
    private String billId;
    private String billName;
    private String billStage;
    private String billProposers;
    private String billBriefSummary;

    public static BillOutlineDto from(Bill bill) {
        return BillOutlineDto.builder()
                .billId(bill.getId())
                .billName(bill.getBillName())
                .billStage(bill.getStage())
                .billProposers(bill.getProposers())
                .billBriefSummary(bill.getBriefSummary())
                .build();
    }
}
