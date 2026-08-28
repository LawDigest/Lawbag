package com.everyones.lawmaking.common.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BillStageDfRequest {

    private String billId;

    private String stage;

    private String committee;

    private String actStatusValue;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate statusUpdateDate;
}
