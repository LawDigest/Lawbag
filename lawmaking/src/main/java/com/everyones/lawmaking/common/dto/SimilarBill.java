package com.everyones.lawmaking.common.dto;

import com.everyones.lawmaking.domain.entity.Bill;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SimilarBill {
    @NotNull
    private String billId;
    @NotNull
    private String billName;

    public static SimilarBill from(Bill bill) {
        return SimilarBill.builder()
                .billId(bill.getId())
                .billName(bill.getBriefSummary() + " (" + bill.getProposers() + ")")
                .build();
    }

}
