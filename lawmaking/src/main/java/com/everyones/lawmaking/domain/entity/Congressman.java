package com.everyones.lawmaking.domain.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;


@Entity
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Congressman {
    @Version
    private Long version;

    @Id
    @Column(name = "congressman_id")
    private String id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;

    @OneToMany(mappedBy = "congressman")
    private List<BillProposer> billPublicProposer;

    @OneToMany(mappedBy = "congressman")
    private List<RepresentativeProposer> representativeProposer;

    @OneToMany(mappedBy = "congressman")
    private List<CongressManLike> congressManLike;

    @Column(name = "elect_sort")
    private String electSort;

    private String district;

    @Column(name = "commits", columnDefinition = "TEXT")
    private String commits;

    @Column(name = "elected")
    private String elected;

    @Column(name = "homepage")
    private String homepage;

    @ColumnDefault("0")
    @Column(name = "represent_count")
    private int representCount;

    @ColumnDefault("0")
    @Column(name = "public_count")
    private int publicCount;

    @Column(name = "congressman_image_url")
    private String congressmanImageUrl;

    @ColumnDefault("0")
    private int likeCount;

    @ColumnDefault("22")
    @Column(name = "assembly_number")
    private int assemblyNumber;

    @Override
    public String toString() {
        return "Congressman{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
