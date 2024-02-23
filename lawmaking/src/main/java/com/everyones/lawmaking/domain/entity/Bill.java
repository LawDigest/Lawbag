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
    /**
    TODO: Bill이랑 Party가 왜 연관관계 매핑이 되어 있을까 연관관계 없애야하지 않을까
     반정규화로 갖고 있어도 괜찮을 수 있을 것 같다.
    **/
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

    @OneToOne(mappedBy = "bill", fetch = FetchType.LAZY)
    private RepresentativeProposer representativeProposer;

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
