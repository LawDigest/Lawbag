package com.everyones.lawmaking.common.dto;

import com.everyones.lawmaking.domain.entity.Congressman;
import lombok.*;

import java.util.List;
@Data
@Builder
//@AllArgsConstructor
@NoArgsConstructor
public class CongressmanDto {
    private String id;
    private String name;

    public CongressmanDto(String id, String name, String partyName, String partyImageUrl, String electSort, String district, String commits, String elected, String homepage, int representCount, int publicCount, List<CongressDetailBillDto> representativeBills) {
        this.id = id;
        this.name = name;
        this.partyName = partyName;
        this.partyImageUrl = partyImageUrl;
        this.electSort = electSort;
        this.district = district;
        this.commits = commits;
        this.elected = elected;
        this.homepage = homepage;
        this.representCount = representCount;
        this.publicCount = publicCount;
        this.representativeBills = representativeBills;
    }

    private String partyName; // Retrieved from Party entity using party_id
    private String partyImageUrl; // Retrieved from Party entity using party_id
    private String electSort;
    private String district;
    private String commits;
    private String elected;
    private String homepage;
    private int representCount;
    private int publicCount;
    private List<CongressDetailBillDto> representativeBills;
    // List of bills the congressman is a representative proposer of
    // CongressmanDto 생성 메소드
    private CongressmanDto buildCongressmanDto(Congressman congressman) {
        return CongressmanDto.builder()
                .id(congressman.getId())
                .name(congressman.getName())
                .partyName(congressman.getParty().getName())
                .partyImageUrl(congressman.getParty().getPartyImageUrl())
                .electSort(congressman.getElectSort())
                .district(congressman.getDistrict())
                .commits(congressman.getCommits())
                .elected(congressman.getElected())
                .homepage(congressman.getHomepage())
                .representCount(congressman.getRepresentCount())
                .publicCount(congressman.getPublicCount())
                .build();
    }
}
