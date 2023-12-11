package com.everyones.lawmaking.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Congressman {
    @Id
    @Column(name = "congressman_id")
    private String id;

    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;

    @Builder.Default
    @OneToMany(mappedBy = "congressman")
    private List<BillProposer> billPublicProposer = new ArrayList<>();

    @Column(name = "elect_sort")
    private String electSort;

    private String district;

    @Column(name = "commits", columnDefinition = "TEXT")
    private String commits;

    @Column(name = "elected")
    private String elected;

    @Column(name = "homepage")
    private String homepage;

    @Column(name = "represent_count")
    private int representCount;

    @Column(name = "public_count")
    private int publicCount;
}
