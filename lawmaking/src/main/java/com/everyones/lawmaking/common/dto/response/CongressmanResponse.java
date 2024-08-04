package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.domain.entity.Congressman;
import com.everyones.lawmaking.global.util.DateUtil;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@Builder
@Data
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CongressmanResponse {
    // Congressman 관련 필드들
    private String congressmanId;
    private String congressmanName;
    private String electSort;
    private String district;
    private String commits;
    private String elected;
    private String homepage;
    private String gender;
    private Integer age;
    private String email;
    private String office;
    private Integer representCount;
    private Integer publicCount;
    private String congressmanImageUrl;
    private Boolean likeChecked;
    // Party 관련 필드들
    private Long partyId;
    private String partyName;
    private String partyImageUrl;




    public static CongressmanResponse from(Congressman congressman)  {
        return CongressmanResponse.builder()
                .congressmanId(congressman.getId())
                .congressmanName(congressman.getName())
                .partyId(congressman.getParty().getId())
                .partyName(congressman.getParty().getName())
                .partyImageUrl(congressman.getParty().getPartyImageUrl())
                .electSort(congressman.getElectSort())
                .district(congressman.getDistrict())
                .commits(congressman.getCommits())
                .elected(congressman.getElected())
                .homepage(congressman.getHomepage())
                .representCount(congressman.getRepresentCount())
                .publicCount(congressman.getPublicCount())
                .congressmanImageUrl(congressman.getCongressmanImageUrl())
                .age(DateUtil.calculateAge(congressman.getCongressmanAge()))
                .email(congressman.getEmail())
                .gender(congressman.getSex())
                .office(congressman.getCongressmanOffice())
                .likeChecked(false)
                .build();
    }


}
