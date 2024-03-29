package com.everyones.lawmaking.common.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Builder
@Getter
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BillDto {

    @NotNull
    private BillInfoDto billInfoDto;

    @NotNull
    private RepresentativeProposerDto representativeProposerDto;

    @NotNull
    private List<PublicProposerDto> publicProposerDtoList;


}