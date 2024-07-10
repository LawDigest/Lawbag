package com.everyones.lawmaking.domain.entity;

import com.everyones.lawmaking.common.dto.request.LawmakerDfRequest;
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
    private List<CongressmanLike> congressmanLike;

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


    public static Congressman of(LawmakerDfRequest lawmakerDfRequest,Party party){
        String electSort = Congressman.getElectSort(lawmakerDfRequest.getDistrict());
        String congressmanId = lawmakerDfRequest.getCongressmanId();
        int assemblyNumber = lawmakerDfRequest.getAssemblyNumber();
        String congressmanImageUrl = "/congressman/" + assemblyNumber + "/" + congressmanId + ".jpg";
        return Congressman.builder()
                .id(congressmanId)
                .name(lawmakerDfRequest.getCongressmanName())
                .party(party)
                .assemblyNumber(22)
                .commits(lawmakerDfRequest.getCommits())
                .elected(lawmakerDfRequest.getElected())
                .homepage(lawmakerDfRequest.getHomepage())
                .district(lawmakerDfRequest.getDistrict())
                .electSort(electSort)
                .congressmanImageUrl(congressmanImageUrl)
                .build();

    }

    public Congressman update(LawmakerDfRequest lawmakerDfRequest,Party party){
        String electSort = Congressman.getElectSort(lawmakerDfRequest.getDistrict());
        this.setId(lawmakerDfRequest.getCongressmanId());
        this.setName(lawmakerDfRequest.getCongressmanName());
        this.setParty(party);
        this.setAssemblyNumber(22);
        this.setCommits(lawmakerDfRequest.getCommits());
        this.setElected(lawmakerDfRequest.getElected());
        this.setHomepage(lawmakerDfRequest.getHomepage());
        this.setDistrict(lawmakerDfRequest.getDistrict());
        this.setElectSort(electSort);
        return this;

    }

    private static String getElectSort(String district) {
        return district.equals("비례대표") ? "비례대표" : "지역구";
    }


}
