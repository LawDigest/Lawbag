package com.everyones.lawmaking.common.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DistrictIdResponse {

    @NotNull
    private Long districtId;

    public static DistrictIdResponse from(Long districtId){
        return DistrictIdResponse.builder()
                .districtId(districtId)
                .build();
    }

}
