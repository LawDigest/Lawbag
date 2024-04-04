package com.everyones.lawmaking.domain.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Data
@ToString
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Party extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id")
    private long id;


    @OneToMany(mappedBy = "party")
    private List<Congressman> congressmanList;

    @OneToMany(mappedBy = "party")
    private List<PartyFollow> partyFollow;

    @OneToMany(mappedBy = "party")
    private List<ProportionalCandidate> proportionalCandidate;

    private String name;

    @Column(name = "party_image_url")
    private String partyImageUrl;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "follow_count")
    private int followCount;

    @ColumnDefault("0")
    @Column(name = "proportional_congressman_count")
    private int proportionalCongressmanCount;

    @ColumnDefault("0")
    @Column(name = "district_congressman_count")
    private int districtCongressmanCount;

    @ColumnDefault("0")
    @Column(name = "total_bill_count")
    private int totalBillCount;

    @ColumnDefault("0")
    @Column(name = "representative_bill_count")
    private int representativeBillCount;

    @ColumnDefault("0")
    @Column(name = "public_bill_count")
    private int publicBillCount;

}
