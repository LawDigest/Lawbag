package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.domain.entity.DistrictCandidate;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DistrictCandidateDetailResponse implements CandidateDetailResponse {

    @NotNull
    private Long DistrictCandidateId;

    private String DistrictCandidateImage;

    private String name;

    //소속
    private String cityName;

    private String districtName;


    private String partyImageUrl;

    private String career1;

    private String career2;

    private String gender;


    private int age;

    private String edu;



    public static DistrictCandidateDetailResponse from(DistrictCandidate districtCandidate) {

        var party = districtCandidate.getParty();
        return DistrictCandidateDetailResponse.builder()
                .DistrictCandidateId(districtCandidate.getId())
                .DistrictCandidateImage(districtCandidate.getImage_url())
                .name(districtCandidate.getName())
                .cityName(districtCandidate.getCityName())
                .districtName(districtCandidate.getDistrictName())
                .partyImageUrl(party.getPartyImageUrl())
                .career1(districtCandidate.getCareer1())
                .career2(districtCandidate.getCareer2())
                .gender(districtCandidate.getGender())
                .age(districtCandidate.getAge())
                .edu(districtCandidate.getEdu())
                .build();

    }
}