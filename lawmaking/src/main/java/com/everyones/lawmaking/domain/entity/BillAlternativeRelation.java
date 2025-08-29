package com.everyones.lawmaking.domain.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Table(
        uniqueConstraints = @UniqueConstraint(
                name = "uq_bill_included",
                columnNames = { "bill_id", "included_bill_id" }
        )
)
public class BillAlternativeRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bill_alternative_relation_id")
    private long id;

    //대안법안
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id")
    private Bill bill;

    //포함된 법안
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "included_bill_id")
    private Bill includedBill;

    public static BillAlternativeRelation create(Bill committeeBill, Bill includedBill ){
        return BillAlternativeRelation.builder()
                .bill(committeeBill)
                .includedBill(includedBill)
                .build();
    }


}
