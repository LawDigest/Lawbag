package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.common.dto.CongressDetailBillDto;
import com.everyones.lawmaking.domain.entity.Bill;
import com.everyones.lawmaking.domain.entity.BillProposer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillProposerRepository extends JpaRepository<BillProposer, Long> {

    List<BillProposer> findByBillId(String billId);
    // In BillProposerRepository interface
    Page<BillProposer> findRepresentativeByCongressmanId(String congressmanId, Pageable pageable);

    List<BillProposer> findByBillIdAndIsRepresent(String billId, boolean isRepresent);

    @Query("SELECT bp FROM BillProposer bp WHERE bp.congressman.id = :congressmanId AND bp.isRepresent = false")
    Page<BillProposer> findCoSponsorshipsByCongressmanId(@Param("congressmanId") String congressmanId, Pageable pageable);


    @Query("SELECT DISTINCT c.id AS congressmanId, " +
            "                c.name AS congressmanName, " +
            "                 p.name AS partyName, " +
            "                 p.id AS partyId " +
            "FROM BillProposer bp " +
            "JOIN Congressman c ON bp.congressman.id = c.id " +
            "JOIN Party p ON c.party.id = p.id " +
            "WHERE bp.bill.id = :billId AND bp.isRepresent = false")
    List<String[]> findPartyByBill(@Param("billId") String billId);
    @Query("SELECT bp.bill.id AS billId, " +
            "       p.id AS partyId, " +
            "       p.name AS partyName " +
            "FROM BillProposer bp " +
            "INNER JOIN bp.bill b " +
            "INNER JOIN b.party p " +
            "WHERE bp.bill.id IN :billIds ")
    List<Object[]> findPartyByBills(@Param("billIds") List<String> billIds);

    @Query("SELECT bp.bill.id FROM BillProposer bp WHERE bp.congressman.id = :congressmanId AND bp.isRepresent = true")
    List<String> findRepresentativeBillIdsByCongressmanId(@Param("congressmanId") String congressmanId);


}
