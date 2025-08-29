package com.everyones.lawmaking.common.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VoteDfRequest {

    private String billId;

    private Integer totalVoteCount;

    private Integer voteForCount;

    private Integer voteAgainstCount;

    private Integer abstentionCount;
}
