package com.everyones.lawmaking.domain.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Builder
@Getter
@Table(name = "RepresentativeProposer")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RepresentativeProposer  extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "representative_proposer_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Bill bill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "congressman_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Congressman congressman;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Party party;

}
