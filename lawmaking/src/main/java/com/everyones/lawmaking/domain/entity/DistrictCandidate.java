package com.everyones.lawmaking.domain.entity;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DistrictCandidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "district_candidate_id")
    private long id;

    @Column(name = "candidate_code")
    private long candidateCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
    private District district;

    @Column(name = "image_url")
    private String image_url;

    @Column(name = "assembly_number")
    private int assembly_number;

    @Column(name = "district_name")
    private String districtName;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "gu_name")
    private String guName;

    @Column(name = "candidate_order")
    private int candidateOrder;

    @Column(name = "party_name")
    private String partyName;

    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birthday")
    private String birthday;

    @Column(name = "age")
    private int age;

    @Column(name = "address")
    private String address;

    @Column(name = "job")
    private String job;

    @Column(name = "edu")
    private String edu;

    @Column(name = "career1")
    private String career1;

    @Column(name = "career2")
    private String career2;

    @Column(name = "status")
    private String status;
}
