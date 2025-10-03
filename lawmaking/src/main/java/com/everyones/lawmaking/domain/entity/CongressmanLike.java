package com.everyones.lawmaking.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "CongressmanLike",
        uniqueConstraints = @UniqueConstraint(name = "ix_congressman_like_unique", columnNames = {"user_id", "congressman_id"}))
public class CongressmanLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "congressman_like_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "congressman_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Congressman congressman;


}
