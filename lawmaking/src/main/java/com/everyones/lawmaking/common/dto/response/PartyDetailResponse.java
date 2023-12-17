package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.common.dto.BillDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PartyDetailResponse {
    private PartyDetailDto partyDetailDto;

    private List<BillDto> bills;

}
