package com.everyones.lawmaking.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
}
