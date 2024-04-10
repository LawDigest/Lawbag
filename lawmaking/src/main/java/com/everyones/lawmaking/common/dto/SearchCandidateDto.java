package com.everyones.lawmaking.common.dto;

import com.everyones.lawmaking.common.dto.response.SearchResponse;
import com.everyones.lawmaking.domain.entity.Candidate;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchCandidateDto implements SearchResponse {

    @NotNull
    private String candidateId;

    @NotNull
    private Long districtCandidateId;

    @NotNull
    private Long proportionalCandidateId;

    private String candidateImageUrl ;

    @NotNull
    private String cityName;

    @NotNull
    private String districtName;

    @NotNull
    private String guName;

    @NotNull
    private String name;

    @NotNull
    private String partyName;

    @NotNull
    private Boolean isDistrict;

    public static SearchResponse from(Candidate candidate) {
        return SearchCandidateDto.builder()
                .candidateId(candidate.getCandidateId())
                .districtCandidateId(candidate.getDistrictCandidateId())
                .proportionalCandidateId(candidate.getProportionalCandidateId())
                .candidateImageUrl(candidate.getCandidateImageUrl())
                .cityName(candidate.getCityName())
                .districtName(candidate.getDistrictName())
                .guName(candidate.getGuName())
                .name(candidate.getName())
                .partyName(candidate.getPartyName())
                .isDistrict(candidate.isDistrict())
                .build();
    }
}
