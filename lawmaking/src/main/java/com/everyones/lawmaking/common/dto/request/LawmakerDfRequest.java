package com.everyones.lawmaking.common.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LawmakerDfRequest {

    private String congressmanId;
    private String congressmanName;
    private String congressmanImageUrl;
    private String commits;
    private String partyName;
    private String elected;
    private String homepage;
    private String district;
    private int assemblyNumber;
}
