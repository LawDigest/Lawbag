package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.domain.entity.Party;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PartyRepository extends JpaRepository<Party, Long> {


    @Query("SELECT p " +
            "FROM Party p " +
            "WHERE p.id = :partyId")
    Optional<Party> findPartyDetailById(@Param("partyId") long partyId);

    @Query("SELECT p FROM Party p " +
            "JOIN p.partyFollow pf " +
            "WHERE pf.user.id = :userId")
    List<Party> findFollowingPartyByUserId(@Param("userId") long userId);

    @Query("SELECT p FROM Party p " +
            "WHERE p.name LIKE %:searchWord% ")
    List<Party> findBySearchWord(@Param("searchWord") String searchWord);

    @Query("select distinct pc.party " +
            "FROM ProportionalCandidate pc ")
    Slice<Party> findProportionalParty(Pageable pageable);

    @Query("SELECT distinct p " +
            "from Party p " +
            "join Congressman c on c.party.id = p.id " +
            "join RepresentativeProposer rp on rp.congressman.id =c.id " +
            "where rp.bill.id =:billId")
    Optional<Party> findPartyByBillId(@Param("billId") String billId);



}
