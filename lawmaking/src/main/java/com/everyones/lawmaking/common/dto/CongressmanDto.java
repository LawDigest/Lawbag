package com.everyones.lawmaking.common.dto;

import com.everyones.lawmaking.domain.entity.Congressman;
import lombok.*;

import java.util.List;
@Builder
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CongressmanDto {
    private String congressmanId;
    private String name;
    private String partyName; // Assume that we get the party name from the 'Party' entity
    private String electName;
    private String oriName;
    private String commits;
    private String elected;
    private String homepage;
    private int representCount;
    private int publicCount;
    private List<BillDto> bills; // List of BillDto objects to represent the bills proposed by the congressman
}
