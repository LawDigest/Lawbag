package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.domain.entity.Party;
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
public class ProportionalPartyResponse {
    // 입력받은 partyId로 정당 로고, 후보자 명수, 정당 이름, 기호몇번인지 조회(partyId)


    @NotNull
    private Long partyId;

    private String partyImageUrl;

    @NotNull
    private Long candidateNumber;

    @NotNull
    private String partyName;

    @NotNull
    private Long partyOrder;

    public static ProportionalPartyResponse of(Party party, Long candidateNumber) {
        return ProportionalPartyResponse.builder()
                .partyId(party.getId())
                .partyImageUrl(party.getPartyImageUrl())
                .candidateNumber(candidateNumber)
                .partyName(party.getName())
                .partyOrder(party.getId())
                .build();
    }


}
