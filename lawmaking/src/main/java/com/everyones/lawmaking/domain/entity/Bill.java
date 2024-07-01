package com.everyones.lawmaking.domain.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.List;


@Entity
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Table(name = "Bill", indexes = @Index(name = "idx_bill_id_propose_date", columnList = "propose_date DESC"))
public class Bill {
    @Id
    @Column(name = "bill_id")
    private String id;

    @Column(name = "bill_number", unique = true)
    private int billNumber;

    // 법안의 제안시점 정당 데이터용으로 partyId 필요
    @Column(name = "partyIdList")
    @ElementCollection(fetch = FetchType.LAZY)
    private List<Long> partyIdList;

    @NotNull
    @Column(name = "assembly_number")
    private int assemblyNumber;

    @Column(name = "bill_name")
    private String billName;

    @Column(name = "proposers")
    private String proposers;

    @OneToMany(mappedBy = "bill")
    private List<BillProposer> publicProposer;

    @OneToMany(mappedBy = "bill", fetch = FetchType.LAZY)
    private List<RepresentativeProposer> representativeProposer;

    @OneToMany(mappedBy = "bill")
    private List<BillLike> billLike;

    private String committee;

    @Column(name = "propose_date")
    private LocalDate proposeDate;

    @Column(name = "stage")
    private String stage;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(name = "gpt_summary", columnDefinition = "TEXT")
    private String gptSummary;

    private String keyword;

    @Column(name = "bill_pdf_url")
    private String billPdfUrl; // PDF 파일의 경로 또는 식별자

    @Column(name = "brief_summary", columnDefinition = "TEXT")
    private String briefSummary;

    @Column(name ="bill_link")
    private String billLink;

    @Column(name ="age")
    @ColumnDefault("0")
    private int age;

    @ColumnDefault("0")
    private int viewCount;

}
