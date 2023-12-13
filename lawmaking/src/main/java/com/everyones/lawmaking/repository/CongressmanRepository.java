package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.domain.entity.Bill;
import com.everyones.lawmaking.domain.entity.Congressman;
import com.everyones.lawmaking.domain.entity.Party;
import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CongressmanRepository extends JpaRepository<Congressman, Long> {

    Congressman findByName(String name);
    List<Congressman> findByNameIn(String[] names);

//    Congressman findById(String[] id);
    @Query("SELECT p.name FROM Congressman c JOIN c.party p WHERE c.id = :congressmanId")
    String findPartyNameByCongressmanId(@Param("congressmanId") String congressmanId);

    @Query("SELECT c FROM Congressman c WHERE c.party.name = :partyName")
    List<Congressman> findAllByPartyName(String partyName);

    @Query("SELECT c FROM Congressman c JOIN FETCH c.billPublicProposer WHERE c.id = :congressmanId")
    Congressman findDetailedCongressman(String congressmanId);

    @Query("SELECT c FROM Congressman c WHERE c.id = :id")
    Congressman findCongressmanById(String id);

    @Query("SELECT bp.bill FROM BillProposer bp WHERE bp.congressman.id = :id")
    List<Bill> findAllBillsByCongressmanId(String id);
    Optional<Object> findById(String congressmanId);
}

// Bill:
//@ManyToOne(fetch = FetchType.LAZY)
//@JoinColumn(name = "represent_proposer_id")
//private Congressman representProposer;
// -> 대표로 발의한 법안 모두 찾아서(findall)

// Congressman:
//@Id
//@Column(name = "congressman_id")
//private String id;

//    @Column(name = "elect_name")
//    private String electName;
//    @Column(name = "ori_name")
//    private String oriName;
//    @Column(name = "commits", columnDefinition = "TEXT")
//    private String commits;
//    @Column(name = "elected")
//    private String elected;
//    @Column(name = "homepage")
//    private String homepage;
//    @Column(name = "represent_count")
//    private int representCount;
//    @Column(name = "public_count")
//    private int publicCount;


// Party
//    private String name;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "party_id")
//    private Party party;
//->  private String name;

// BillProposer
//@ManyToOne(fetch = FetchType.LAZY)
//@JoinColumn(name = "congressman_id")
//private Congressman congressman;
// ->     @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "bill_id")
//    private Bill bill;
//
//
//