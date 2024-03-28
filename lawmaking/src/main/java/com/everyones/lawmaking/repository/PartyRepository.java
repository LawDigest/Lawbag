package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.common.dto.response.PartyDetailResponse;
import com.everyones.lawmaking.domain.entity.Party;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PartyRepository extends JpaRepository<Party, Long> {


    @Query("SELECT new com.everyones.lawmaking.common.dto.response.PartyDetailResponse(p.id, p.name, p.partyImageUrl, p.websiteUrl, " +
            "SUM(CASE WHEN c.electSort = '지역구' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN c.electSort = '비례대표' THEN 1 ELSE 0 END)) " +
            "FROM Party p " +
            "JOIN Congressman c ON p.id = c.party.id " +
            "WHERE p.id = :partyId")
    Optional<PartyDetailResponse> findPartyDetailById(@Param("partyId") long partyId);

    @Query("SELECT p FROM Party p " +
            "JOIN p.partyFollow pf " +
            "WHERE pf.user.id = :userId")
    List<Party> findFollowingPartyByUserId(@Param("userId") long userId);

    @Query("SELECT p FROM Party p " +
            "WHERE p.name LIKE %:searchWord% ")
    List<Party> findBySearchWord(@Param("searchWord") String searchWord);
}
