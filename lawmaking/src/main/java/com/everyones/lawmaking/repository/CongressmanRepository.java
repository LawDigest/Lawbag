package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.common.dto.CongressDetailBillDto;
import com.everyones.lawmaking.domain.entity.Congressman;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CongressmanRepository extends JpaRepository<Congressman, String> {
    // Existing findByIdWithParty method
    @Query("SELECT c FROM Congressman c LEFT JOIN FETCH c.party WHERE c.id = :congressmanId")
    Optional<Congressman> findByIdWithParty(@Param("congressmanId") String congressmanId);

//    @Query("SELECT new com.everyones.lawmaking.common.dto.CongressDetailBillDto(" +
//            "b.id, b.billName, c.name, c.id, " +
//            "p.name, p.id, p.partyImageUrl, " +
//            "b.proposers, b.summary, b.gptSummary, b.proposeDate, " +
//            "(SELECT p2.name FROM Party p2 WHERE p2.id IN (SELECT c2.party.id FROM Congressman c2 WHERE c2.id IN (SELECT bp2.congressman.id FROM BillProposer bp2 WHERE bp2.bill.id = b.id))), " +
//            "(SELECT p2.id FROM Party p2 WHERE p2.id IN (SELECT c2.party.id FROM Congressman c2 WHERE c2.id IN (SELECT bp2.congressman.id FROM BillProposer bp2 WHERE bp2.bill.id = b.id)))" +
//            ") " +
//            "FROM Bill b JOIN b.publicProposer bp JOIN bp.congressman c JOIN c.party p " +
//            "WHERE bp.isRepresent = true AND c.id = :congressmanId " +
//            "ORDER BY b.proposeDate DESC, b.id DESC")
//    Page<CongressDetailBillDto> findCongressDetailBillDtoByCongressmanId(@Param("congressmanId") String congressmanId, Pageable pageable);

}
