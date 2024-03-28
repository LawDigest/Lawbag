package com.everyones.lawmaking.common.dto;

import com.everyones.lawmaking.common.dto.response.SearchResponse;
import com.everyones.lawmaking.domain.entity.Party;
import com.everyones.lawmaking.global.constant.SearchType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchPartyDto implements SearchResponse {

    @NotNull
    private long id;

    @NotNull
    private String name;

    @NotNull
    private String partyImageUrl;

    @NotNull
    private String searchType;

    public static SearchResponse from(Party party) {
        return SearchPartyDto.builder()
                .id(party.getId())
                .name(party.getName())
                .partyImageUrl(party.getPartyImageUrl())
                .searchType(SearchType.PARTY.name())
                .build();
    }
}
