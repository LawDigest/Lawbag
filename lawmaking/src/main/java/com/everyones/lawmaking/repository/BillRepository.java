package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.common.dto.BillDto;
import com.everyones.lawmaking.domain.entity.Bill;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, String> {
    @Query("SELECT new com.everyones.lawmaking.common.dto.BillDto(b.id, b.billName, bp.congressman.name, b.summary, b.proposeDate)" +
            "FROM Bill b " +
            "JOIN b.publicProposer bp " +
            "WHERE bp.isRepresent = true " +
            "ORDER BY b.proposeDate DESC, b.id DESC")
    List<BillDto> findNextThreeBills(Pageable pageable);



    List<Bill> findByRepresentProposerId(String congressmanId);

    @Query("SELECT b FROM Bill b WHERE b.representProposer.id = :congressmanId OR EXISTS (SELECT bp FROM BillProposer bp WHERE bp.congressman.id = :congressmanId AND bp.bill = b)")
    List<Bill> findAllBillsByCongressmanId(@Param("congressmanId") String congressmanId);

}
