package com.everyones.lawmaking.common.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VotePartyListRequest {

    List<VotePartyRequest> votePartyRequestList;
}
