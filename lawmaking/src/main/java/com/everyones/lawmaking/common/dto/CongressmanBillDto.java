package com.everyones.lawmaking.common.dto;


import com.everyones.lawmaking.domain.entity.Bill;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;


@Builder
@Getter
@AllArgsConstructor  // 전체 생성자 추가
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)

public class CongressmanBillDto {

    private BillInfoDto billInfoDto;
    private RepresentativeProposerDto representativeProposerDto;
    private List<PublicProposerDto> publicProposerDtoList;


}