package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.common.dto.ProportionalCandidateListDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProportionalCandidateListResponse {

    private List<ProportionalCandidateListDto> proportionalCandidateListDtoList;

    private PaginationResponse paginationResponse;

    public static ProportionalCandidateListResponse of(List<ProportionalCandidateListDto> proportionalCandidateListDtoList, PaginationResponse paginationResponse) {
        return ProportionalCandidateListResponse.builder()
                .proportionalCandidateListDtoList(proportionalCandidateListDtoList)
                .paginationResponse(paginationResponse)
                .build();
    }


}
