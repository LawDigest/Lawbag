package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.domain.entity.District;
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
public class DistrictNameResponse implements DistrictResponse{

    @NotNull
    private String districtName;

    public static DistrictResponse from(String districtName){
        return DistrictNameResponse.builder()
                .districtName(districtName)
                .build();
    }

}
