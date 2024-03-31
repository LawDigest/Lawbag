package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.domain.entity.PartyPromise;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyPromiseRepository extends JpaRepository<PartyPromise, Long> {

    @Query("select pp " +
            "from PartyPromise pp " +
            "JOIN FETCH pp.party p " +
            "where pp.party.id = :partyId ")
    Slice<PartyPromise> findPartyPromiseByPartyId(@Param("partyId") long partyId, Pageable pageable);

}
