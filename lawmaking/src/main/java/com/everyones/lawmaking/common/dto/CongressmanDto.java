package com.everyones.lawmaking.common.dto;

import com.everyones.lawmaking.domain.entity.Congressman;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CongressmanDto {
    private String id;
    private String name;
    private String partyName; // Retrieved from Party entity using party_id
    private String partyImageUrl; // Retrieved from Party entity using party_id
    private String electSort;
    private String district;
    private String commits;
    private String elected;
    private String homepage;
    private int representCount;
    private int publicCount;
    private String congressmanImageUrl;
    private List<CongressDetailBillDto> representativeBills;

}
