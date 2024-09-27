package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.domain.entity.VoteParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VotePartyRepository extends JpaRepository<VoteParty,Long> {
    @Query("select vp from VoteParty vp " +
            "join vp.bill b " +
            "join vp.party p " +
            " where b.id =:billId and p.id=:partyId")
    Optional<VoteParty> findByBillAndParty(@Param("billId") String billId, @Param("partyId") Long partyId);
}
