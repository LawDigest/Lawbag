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

    private long publicProposerPartyId;
    private String publicProposerPartyName;
    private String publicProposerPartyImageUrl;

    public static PublicProposerDto from(BillProposer billProposer) {
        var congressman = billProposer.getCongressman();
        var party = congressman.getParty();
        return PublicProposerDto.builder()
                .publicProposerId(congressman.getId())
                .publicProposerName(congressman.getName())
                .publicProposerImgUrl(congressman.getCongressmanImageUrl())
                .publicProposerPartyId(party.getId())
                .publicProposerPartyName(party.getName())
                .publicProposerPartyImageUrl(party.getPartyImageUrl())
                .build();
    }
}
