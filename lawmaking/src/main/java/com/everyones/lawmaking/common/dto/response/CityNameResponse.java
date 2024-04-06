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
public class CityNameResponse implements DistrictResponse{

    @NotNull
    private String cityName;

    public static DistrictResponse from(String cityName){
        return CityNameResponse.builder()
                .cityName(cityName)
                .build();
    }

}
