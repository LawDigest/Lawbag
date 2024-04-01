package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.common.dto.ProportionalPartyImageListDto;
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
public class ProportionalPartyImageListResponse {

    private List<ProportionalPartyImageListDto> ProportionalPartyLogoListDtoList;

    private PaginationResponse paginationResponse;

    public static ProportionalPartyImageListResponse of(List<ProportionalPartyImageListDto> ProportionalPartyLogoListDtoList, PaginationResponse paginationResponse) {
        return ProportionalPartyImageListResponse.builder()
                .ProportionalPartyLogoListDtoList(ProportionalPartyLogoListDtoList)
                .paginationResponse(paginationResponse)
                .build();
    }
}
