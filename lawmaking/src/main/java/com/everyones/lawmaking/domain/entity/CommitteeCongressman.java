package com.everyones.lawmaking.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Table(
        uniqueConstraints = @UniqueConstraint(
                name = "UK_COMMITTEE_CONGRESSMAN",
                columnNames = {"committee_id", "congressman_id"}
        )
)
public class CommitteeCongressman extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "committee_congressman_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "committee_id", nullable = false)
    private Committee committee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "congressman_id", nullable = false)
    private Congressman congressman;

    @Column(name = "position")
    private String position;

    public static CommitteeCongressman create(Committee committee, Congressman congressman   ) {
        return CommitteeCongressman.builder()
                .committee(committee)
                .congressman(congressman) // This should be set later
                .position(committee.getCommitteeName())
                .build();
    }

    public  void updatePosition(String position) {
        this.position = position;
    }

}
