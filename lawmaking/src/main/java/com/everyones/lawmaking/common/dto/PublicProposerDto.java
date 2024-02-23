package com.everyones.lawmaking.common.dto;

import com.everyones.lawmaking.domain.entity.BillProposer;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PublicProposerDto {
    private String publicProposerId;
    private String publicProposerName;
    private String publicProposerImgUrl;

    private long partyId;
    private String partyName;
    private String partyImageUrl;

    public static PublicProposerDto fromPublicProposer(BillProposer billProposer) {
        var congressman = billProposer.getCongressman();
        var party = congressman.getParty();
        return PublicProposerDto.builder()
                .publicProposerId(congressman.getId())
                .publicProposerName(congressman.getName())
                .publicProposerImgUrl(congressman.getCongressmanImageUrl())
                .partyId(party.getId())
                .partyName(party.getName())
                .partyImageUrl(party.getPartyImageUrl())
                .build();
    }
}
