package com.everyones.lawmaking.common.dto;

import com.everyones.lawmaking.domain.entity.Bill;
import com.everyones.lawmaking.domain.entity.BillProposer;
import com.everyones.lawmaking.domain.entity.RepresentativeProposer;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;


@Builder
@Getter
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RepresentativeProposerDto {

    private String representativeProposerId;
    private String representativeProposerName;
    private String representProposerImgUrl;

    private long partyId;
    private String partyImageUrl;
    private String partyName;

    public static RepresentativeProposerDto from(RepresentativeProposer representativeProposer) {
        return RepresentativeProposerDto.builder()
                .representativeProposerId(representativeProposer.getCongressman().getId())
                .representativeProposerName(representativeProposer.getCongressman().getName())
                .representProposerImgUrl(representativeProposer.getCongressman().getCongressmanImageUrl())
                .partyId(representativeProposer.getCongressman().getParty().getId())
                .partyName(representativeProposer.getCongressman().getParty().getName())
                .partyImageUrl(representativeProposer.getCongressman().getParty().getPartyImageUrl())
                .build();
    }


}
