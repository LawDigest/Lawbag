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
public class GuNameResponse implements DistrictResponse{

    @NotNull
    private String guName;

    public static DistrictResponse from(String guName){
        return GuNameResponse.builder()
                .guName(guName)
                .build();
    }

}