package com.everyones.lawmaking.common.dto;

import com.everyones.lawmaking.domain.entity.Party;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProportionalPartyImageListDto {

    @NotNull
    private long partyId;

    private String partyImageUrl;

    private String partyName;

    public static ProportionalPartyImageListDto from(Party party){
        return ProportionalPartyImageListDto.builder()
                .partyId(party.getId())
                .partyImageUrl(party.getPartyImageUrl())
                .partyName(party.getName())
                .build();
    }
}
