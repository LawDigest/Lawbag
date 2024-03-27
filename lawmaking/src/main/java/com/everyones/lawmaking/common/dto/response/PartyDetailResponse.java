package com.everyones.lawmaking.common.dto.response;

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
    private int propotionalRepresentativeCount;
    @NotNull
    private int districtRepresentativeCount;
    @NotNull
    private boolean isFollowed;
    @NotNull
    private String websiteUrl;

    // TODO: isFollowed의 경우, User를 거치는 경우와 안 거치는 경우를 나눠서 해야함.
    public PartyDetailResponse(long partyId, String partyName, String partyImgUrl, int districtRepresentativeCount, int propotionalRepresentativeCount, String websiteUrl) {
        this.partyId = partyId;
        this.partyName= partyName;
        this.partyImgUrl= partyImgUrl;
        this.propotionalRepresentativeCount = propotionalRepresentativeCount;
        this.districtRepresentativeCount = districtRepresentativeCount;
        this.websiteUrl = websiteUrl;
        this.isFollowed = false;
    }

}
