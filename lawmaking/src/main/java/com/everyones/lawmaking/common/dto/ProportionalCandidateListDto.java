package com.everyones.lawmaking.common.dto;

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
public class ProportionalCandidateListDto {

    @NotNull
    private long ProportionalCandidateId;

    private String name;

    private Integer candidateOrder;

    private String career1;

    private String proportionalCandidateImageUrl;

    public static ProportionalCandidateListDto from(ProportionalCandidate proportionalCandidate){
        return ProportionalCandidateListDto.builder()
                .ProportionalCandidateId(proportionalCandidate.getId())
                .name(proportionalCandidate.getName())
                .candidateOrder(proportionalCandidate.getCandidateOrder())
                .career1(proportionalCandidate.getCareer1())
                .proportionalCandidateImageUrl(proportionalCandidate.getImage_url())
                .build();
    }
}
