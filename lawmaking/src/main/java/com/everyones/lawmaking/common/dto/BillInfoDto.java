package com.everyones.lawmaking.common.dto;

import com.everyones.lawmaking.domain.entity.Bill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BillInfoDto {

    private String billId;
    private String billName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate proposeDate;
    private String summary;
    private String gptSummary;


    public static BillInfoDto fromBill(Bill bill) {
        return BillInfoDto.builder()
                .billId(bill.getId())
                .billName(bill.getBillName())
                .proposeDate(bill.getProposeDate())
                .summary(bill.getSummary())
                .gptSummary(bill.getGptSummary())
                .build();
    }

}
