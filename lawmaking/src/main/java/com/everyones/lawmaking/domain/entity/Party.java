package com.everyones.lawmaking.domain.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
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

    @Builder.Default
    @OneToMany(mappedBy = "party")
    private List<Congressman> congressmanList = new ArrayList<>();

    private String name;

    @Column(name = "party_image_url")
    private String partyImageUrl;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "follow_count")
    private int followCount;

}
