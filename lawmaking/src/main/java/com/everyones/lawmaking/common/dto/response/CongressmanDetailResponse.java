package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.common.dto.CongressmanBillDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor

@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CongressmanDetailResponse {
    private PaginationResponse paginationResponse;
    private List<CongressmanBillDto> bills;
}


