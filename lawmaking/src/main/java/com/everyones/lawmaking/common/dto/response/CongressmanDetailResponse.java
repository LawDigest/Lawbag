package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.common.dto.CongressDetailBillDto;
import com.everyones.lawmaking.common.dto.CongressmanDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor

@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CongressmanDetailResponse {
    private CongressmanDto congressman;
    private PaginationResponse paginationResponse;
    private List<CongressDetailBillDto> bills;
}


