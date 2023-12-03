package com.everyones.lawmaking.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;

    @NotNull
    private int age;

    @Column(name = "bill_name")

    private String billName;

    @Column(name = "represent_proposer")
    private String representProposer;

    @Column(name = "public_proposer")
    private String publicProposer;

    private String committe;

    @Column(name = "propose_date")
    private LocalDateTime proposeDate;

    private String status;

    @Lob
    private String summary;

    @Lob
    @Column(name = "gpt_summary")
    private String gptSummary;

    private String keyword;
}
