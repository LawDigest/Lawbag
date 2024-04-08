package com.everyones.lawmaking.common.dto;

import com.everyones.lawmaking.domain.entity.Party;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProportionalCountByPartyDto {
    private Party party;

    private Long candidateCount;

    public ProportionalCountByPartyDto (Party party,Long candidateCount) {
        this.party =party;
        this.candidateCount = candidateCount;

    }
}
