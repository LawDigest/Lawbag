package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.domain.entity.Party;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
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
    private int representativeBillCount;
    @NotNull
    private int publicBillCount;
    @NotNull
    private int followCount;
    @NotNull
    private boolean isFollowed;
    @NotNull
    private String websiteUrl;


    public static PartyDetailResponse from(Party party) {
        var partyFollow = party.getPartyFollow();
        return PartyDetailResponse.builder()
                .partyId(party.getId())
                .partyName(party.getName())
                .partyImgUrl(party.getPartyImageUrl())
                .websiteUrl(party.getWebsiteUrl())
                .proportionalCongressmanCount(party.getProportionalCongressmanCount())
                .districtCongressmanCount(party.getDistrictCongressmanCount())
                .representativeBillCount(party.getRepresentativeBillCount())
                .publicBillCount(party.getPublicBillCount())
                .followCount(partyFollow.size())
                .isFollowed(false)
                .build();
    }

}
