package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.common.dto.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BillDetailResponse extends BillDto {
    private List<SimilarBill> similarBills;


    public BillDetailResponse(@NotNull BillInfoDto billInfoDto, @NotNull RepresentativeProposerDto representativeProposerDto, @NotNull List<PublicProposerDto> publicProposerDtoList) {
        super(billInfoDto, representativeProposerDto, publicProposerDtoList);
    }
    public BillDetailResponse(@NotNull BillInfoDto billInfoDto, @NotNull RepresentativeProposerDto representativeProposerDto, @NotNull List<PublicProposerDto> publicProposerDtoList, List<SimilarBill> similarBills) {
        super(billInfoDto, representativeProposerDto, publicProposerDtoList);
        this.similarBills = similarBills;
    }

    public void setSimilarBills(List<SimilarBill> similarBills) {
        this.similarBills = similarBills;
    }
}
