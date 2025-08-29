package com.everyones.lawmaking.common.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommitteeDfRequest {
    private String committeeDivCode;

    private String committeeDivName;

    private String committeeDeptCode;

    private String committeeName;

    private String chairmanName;

    private String chairmanId;

    private List<String> congressmanIdList;

    private int memberLimitCount;

    private int committeeMemberCount;

    private int nonNegotiatingMemberCount;

    private int negotiatingMemberCount;

}
