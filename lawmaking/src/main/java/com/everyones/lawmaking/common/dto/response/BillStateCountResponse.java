package com.everyones.lawmaking.common.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BillStateCountResponse {

    @Schema(name = "접수 법안")
    private Integer receiptCount;
    @Schema(name = "처리 법안")
    private Integer treatmentCount;
    @Schema(name = "가결 법안")
    private Integer passedCount;

}
