package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.domain.entity.BillLike;
import com.everyones.lawmaking.domain.entity.ProportionalCandidate;
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
public class ProportionalCandidateDetailResponse implements CandidateDetailResponse {

    @NotNull
    private Long proportionalCandidateId;

    private String proportionalCandidateImage;

    private String name;

    //소속
    private String partyName;


    private String partyImageUrl;

    private String career1;

    private String career2;

    private String gender;


    private int age;

    private String edu;



    public static ProportionalCandidateDetailResponse from(ProportionalCandidate proportionalCandidate) {

        var party = proportionalCandidate.getParty();
        return ProportionalCandidateDetailResponse.builder()
                .proportionalCandidateId(proportionalCandidate.getId())
                .proportionalCandidateImage(proportionalCandidate.getImage_url())
                .name(proportionalCandidate.getName())
                .partyName(party.getName())
                .partyImageUrl(party.getPartyImageUrl())
                .career1(proportionalCandidate.getCareer1())
                .career2(proportionalCandidate.getCareer2())
                .gender(proportionalCandidate.getGender())
                .age(proportionalCandidate.getAge())
                .edu(proportionalCandidate.getEdu())
                .build();

    }
}

