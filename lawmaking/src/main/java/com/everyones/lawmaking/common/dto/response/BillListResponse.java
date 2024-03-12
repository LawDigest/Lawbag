package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.common.dto.BillDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BillListResponse {
    private PaginationResponse paginationResponse;

    private List<BillDto> billList;
}
