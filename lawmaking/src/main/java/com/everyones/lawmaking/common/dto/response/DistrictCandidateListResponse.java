package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.common.dto.DistrictCandidateListDto;
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
public class DistrictCandidateListResponse {

    private List<DistrictCandidateListDto> districtCandidateListDtoList;

    private PaginationResponse paginationResponse;

    public static DistrictCandidateListResponse of(List<DistrictCandidateListDto> districtCandidateListDtoList, PaginationResponse paginationResponse) {
        return DistrictCandidateListResponse.builder()
                .districtCandidateListDtoList(districtCandidateListDtoList)
                .paginationResponse(paginationResponse)
                .build();
    }


}
