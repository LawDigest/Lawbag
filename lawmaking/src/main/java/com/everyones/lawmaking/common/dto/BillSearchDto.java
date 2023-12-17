package com.everyones.lawmaking.common.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BillSearchDto {
    private String id;
    private String summary;

    public BillSearchDto(String id, String summary) {
        this.id = id;
        this.summary = summary;
    }

    // Getters and Setters
}
