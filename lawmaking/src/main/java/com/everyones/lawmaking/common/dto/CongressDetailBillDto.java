package com.everyones.lawmaking.common.dto;


import com.everyones.lawmaking.domain.entity.BillProposer;
import com.everyones.lawmaking.repository.BillProposerRepository;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;

import java.util.List;
import java.util.stream.Collectors;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor  // 전체 생성자 추가
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CongressDetailBillDto {


    private String billId;
    private String billName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate proposeDate;
    private String proposers;
    private String summary;
    private String gptSummary;
    private String representProposer;
    private String representProposerId;
    private String representProposerParty;
    private long representProposerPartyId;
    private String representProposerImgUrl;
    private List<String> partyList;
    private List<Long> partyIdList;

    public CongressDetailBillDto(String billId, String billName,
                                 LocalDate proposeDate, String proposers,
                                 String summary, String gptSummary,
                                 String representProposer, String representProposerId,
                                 String representProposerParty, long representProposerPartyId,
                                 String representProposerImgUrl) {
        this.billId = billId;
        this.billName = billName;
        this.proposeDate = proposeDate;
        this.proposers = proposers;
        this.summary = summary;
        this.gptSummary = gptSummary;
        this.representProposer = representProposer;
        this.representProposerId = representProposerId;
        this.representProposerParty = representProposerParty;
        this.representProposerPartyId = representProposerPartyId;
        this.representProposerImgUrl = representProposerImgUrl;

    }
}