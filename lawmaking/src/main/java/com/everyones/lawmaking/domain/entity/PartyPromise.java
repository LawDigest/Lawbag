package com.everyones.lawmaking.domain.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PartyPromise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_promise_id")
    private long id;

    @OneToOne
    @JoinColumn(name = "party_id")
    private Party party;

    @Column(name = "party_name")
    private String partyName;

    @Column(name = "assembly_number")
    private int assemblyNumber;

    @Column(name = "promise_order")
    private int promiseOrder;

    private String realm;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;


}
