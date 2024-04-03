package com.everyones.lawmaking.common.dto;

import com.everyones.lawmaking.domain.entity.DistrictCandidate;
import com.everyones.lawmaking.domain.entity.ProportionalCandidate;
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
public class DistrictCandidateListDto {

    @NotNull
    private long districtCandidateId;

    private String name;

    private Integer candidateOrder;

    private String partyName;

    private String cityName;

    private String districtName;

    private String districtCandidateImageUrl;

    public static DistrictCandidateListDto from(DistrictCandidate districtCandidate){
        return DistrictCandidateListDto.builder()
                .districtCandidateId(districtCandidate.getId())
                .name(districtCandidate.getName())
                .candidateOrder(districtCandidate.getCandidateOrder())
                .partyName(districtCandidate.getPartyName())
                .cityName(districtCandidate.getCityName())
                .districtName(districtCandidate.getDistrictName())
                .districtCandidateImageUrl(districtCandidate.getImage_url())
                .build();
    }
}
