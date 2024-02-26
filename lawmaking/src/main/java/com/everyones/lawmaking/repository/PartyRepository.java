package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.common.dto.response.PartyDetailDto;
import com.everyones.lawmaking.domain.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PartyRepository extends JpaRepository<Party, Long> {
//    Party findByName(String name);

    @Query("SELECT new com.everyones.lawmaking.common.dto.response.PartyDetailDto(p.id, p.name, p.partyImageUrl, " +
            "SUM(CASE WHEN c.electSort = '지역구' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN c.electSort = '비례대표' THEN 1 ELSE 0 END)) " +
            "FROM Party p " +
            "JOIN Congressman c ON p.id = c.party.id " +
            "WHERE p.id = :partyId")
    Optional<PartyDetailDto> findPartyDetailById(@Param("partyId") long partyId);



}
