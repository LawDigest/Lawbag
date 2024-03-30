package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.domain.entity.Party;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PartyDetailResponse {

    @NotNull
    private long partyId;
    @NotNull
    private String partyName;
    @NotNull
    private String partyImgUrl;
    @NotNull
    private int proportionalCongressmanCount;
    @NotNull
    private int districtCongressmanCount;
    @NotNull
    private boolean isFollowed;
    @NotNull
    private String websiteUrl;

    // TODO: isFollowed의 경우, User를 거치는 경우와 안 거치는 경우를 나눠서 해야함.
    public static PartyDetailResponse from(Party party) {
        return PartyDetailResponse.builder()
                .partyId(party.getId())
                .partyName(party.getName())
                .partyImgUrl(party.getPartyImageUrl())
                .websiteUrl(party.getWebsiteUrl())
                .proportionalCongressmanCount(party.getProportionalCongressmanCount())
                .districtCongressmanCount(party.getDistrictCongressmanCount())
                .isFollowed(false)
                .build();
    }

}
