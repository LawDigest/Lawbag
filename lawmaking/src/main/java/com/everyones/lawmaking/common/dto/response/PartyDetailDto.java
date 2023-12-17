package com.everyones.lawmaking.common.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PartyDetailDto {
    private long partyId;

    private String partyName;

    private String partyImgUrl;

    private long propotionalRepresentativeCount;

    private long districtRepresentativeCount;

    private boolean isFollowed;

    private String websiteUrl;

    public PartyDetailDto(long partyId, String partyName, String partyImgUrl, long districtRepresentativeCount, long propotionalRepresentativeCount) {
        this.partyId = partyId;
        this.partyName= partyName;
        this.partyImgUrl= partyImgUrl;
        this.propotionalRepresentativeCount = propotionalRepresentativeCount;
        this.districtRepresentativeCount = districtRepresentativeCount;
        this.websiteUrl = null;
        this.isFollowed = false;
    }

}
