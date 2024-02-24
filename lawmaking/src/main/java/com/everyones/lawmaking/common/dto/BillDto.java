package com.everyones.lawmaking.common.dto;


import com.everyones.lawmaking.common.dto.BillInfoDto;
import com.everyones.lawmaking.common.dto.PublicProposerDto;
import com.everyones.lawmaking.common.dto.RepresentativeProposerDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Builder
@Getter
@AllArgsConstructor  // 전체 생성자 추가
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)

public class BillDto {

    private BillInfoDto billInfoDto;
    private RepresentativeProposerDto representativeProposerDto;
    private List<PublicProposerDto> publicProposerDtoList;


}