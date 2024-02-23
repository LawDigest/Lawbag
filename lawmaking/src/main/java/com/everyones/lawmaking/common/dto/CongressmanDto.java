package com.everyones.lawmaking.common.dto;

import com.everyones.lawmaking.domain.entity.Congressman;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
@Builder
@Getter
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CongressmanDto {
    // Congressman 관련 필드들
    private String congressmanId;
    private String congressmanName;

    // Party 관련 필드들
    private Long partyId;
    private String partyName;
    private String partyImageUrl;
    private String electSort;
    private String district;
    private String commits;
    private String elected;
    private String homepage;
    private int representCount;
    private int publicCount;
    private String congressmanImageUrl;

    @Builder
    public static CongressmanDto fromCongressman(Congressman congressman)  {
        return CongressmanDto.builder()
                .congressmanId(congressman.getId())
                .congressmanName(congressman.getName())
                .partyId(congressman.getParty().getId())
                .partyName(congressman.getParty().getName())
                .partyImageUrl(congressman.getParty().getPartyImageUrl())
                .electSort(congressman.getElectSort())
                .district(congressman.getDistrict())
                .commits(congressman.getCommits())
                .elected(congressman.getElected())
                .homepage(congressman.getHomepage())
                .representCount(congressman.getRepresentCount())
                .publicCount(congressman.getPublicCount())
                .congressmanImageUrl(congressman.getCongressmanImageUrl())
                .build();
    }


}
