package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.domain.entity.PartyPromise;
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
public class PartyPromiseResponse {

    @NotNull
    private long partyPromiseId;

    private String title;

    private String content;

    public static PartyPromiseResponse from(PartyPromise partyPromise){
        return PartyPromiseResponse.builder()
                .partyPromiseId(partyPromise.getId())
                .title(partyPromise.getTitle())
                .content(partyPromise.getContent())
                .build();
    }

}
