package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.domain.entity.ProportionalCandidate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProportionalCandidateRepository extends JpaRepository<ProportionalCandidate,Long > {

    @Query("select pc " +
            "from ProportionalCandidate pc " +
            "join FETCH pc.party p " +
            "where pc.party.id = :partyId ")
    Slice<ProportionalCandidate> findProportionalCandidateByPartyId(@Param("partyId") long partyId, Pageable pageable);

    Long countByPartyId(long partyId);

    @Query("select pc " +
            "from ProportionalCandidate pc " +
            "join FETCH pc.party p " +
            "where pc.id =:candidateId ")
    Optional<ProportionalCandidate> findProportionalCandidateById(@Param("candidateId") Long candidateId);
}
