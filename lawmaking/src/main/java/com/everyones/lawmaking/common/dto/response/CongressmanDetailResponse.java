package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.common.dto.CongressDetailBillDto;
import com.everyones.lawmaking.common.dto.CongressmanDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.springframework.data.domain.Page;

@Builder
@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CongressmanDetailResponse {
    private CongressmanDto congressman;
    private boolean isLastPage;
    private int pageNumber;

    public CongressmanDetailResponse(CongressmanDto congressman, boolean isLastPage, int pageNumber) {
        this.congressman = congressman;
        this.isLastPage = isLastPage;
        this.pageNumber = pageNumber;
    }
}


