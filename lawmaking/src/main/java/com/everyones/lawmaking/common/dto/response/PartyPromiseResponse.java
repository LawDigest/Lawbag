package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.common.dto.PartyPromiseDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PartyPromiseResponse {

    @NotNull
    private List<PartyPromiseDto> partyPromise;
    @NotNull
    private PaginationResponse paginationResponse;

    public static PartyPromiseResponse of(List<PartyPromiseDto> partyPromiseList, PaginationResponse paginationResponse) {
        return PartyPromiseResponse.builder()
                .partyPromise(partyPromiseList)
                .paginationResponse(paginationResponse)
                .build();
    }
}
