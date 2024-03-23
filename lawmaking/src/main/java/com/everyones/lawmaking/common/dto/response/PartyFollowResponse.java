package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.domain.entity.CongressManLike;
import com.everyones.lawmaking.domain.entity.PartyFollow;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PartyFollowResponse {

    private long partyId;

    private boolean followChecked;

    public static PartyFollowResponse from(PartyFollow partyFollow){
        return PartyFollowResponse.builder()
                .partyId(partyFollow.getParty().getId())
                .followChecked(partyFollow.isFollowChecked())
                .build();
    }
}


