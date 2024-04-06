package com.everyones.lawmaking.domain.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Immutable;


@Entity
@Immutable
@Data
@Table(name = "candidate")
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Candidate {

    @Id
    @Column(name="candidate_id")
    private String candidateId;

    @NotNull
    @Column(name="district_candidate_id")
    private Long districtCandidateId;

    @NotNull
    @Column(name="proportional_candidate_id")
    private Long proportionalCandidateId;

    @NotNull
    @Column(name="city_name")
    private String cityName;

    @NotNull
    @Column(name="district_name")
    private String districtName;

    @NotNull
    @Column(name="gu_name")
    private String guName;

    @NotNull
    @Column(name="name")
    private String name;

    @NotNull
    @Column(name="party_name")
    private String partyName;

    @NotNull
    @Column(name="is_district")
    private boolean isDistrict;

}
