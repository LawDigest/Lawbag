package com.everyones.lawmaking.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bill {
    @Id
    @Column(name = "bill_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;

    @NotNull
    private int age;

    @Column(name = "bill_name")
    private String billName;

    @Column(name = "proposers")
    private String proposers;

    @Builder.Default
    @OneToMany(mappedBy = "bill")
    private List<BillProposer> publicProposer = new ArrayList<>();

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

}
