package com.everyones.lawmaking.common.dto;

import com.everyones.lawmaking.domain.entity.BillTimeline;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BillTimelineDto {

    List<PartyDto> representPartyList;

    String billName;

    String billStage;

    String committee;

    List<BillVoteDto> voteResultList;

    public static BillTimelineDto from(BillTimeline billTimeline) {
        var bill = billTimeline.getBill();
        var representPartyList = bill.getRepresentativeProposer().stream()
                .map(PartyDto::from)
                .toList();
        var voteResultList = bill.getVotePartyList().stream()
                .map(BillVoteDto::from)
                .toList();
        return BillTimelineDto.builder()
                .representPartyList(representPartyList)
                .billName(bill.getBillName())
                .billStage(bill.getStage())
                .committee(bill.getCommittee())
                .voteResultList(voteResultList)
                .build();
    }
    

}
