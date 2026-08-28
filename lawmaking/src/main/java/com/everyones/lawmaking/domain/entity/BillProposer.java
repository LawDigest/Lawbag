package com.everyones.lawmaking.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "BillProposer",
        indexes = {
@Index(name = "idx_bill_id", columnList = "bill_id"),
@Index(name = "idx_congressman_id", columnList = "congressman_id")})
public class BillProposer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_public_proposer_id")
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
