package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.common.dto.BillDto;
import com.everyones.lawmaking.common.dto.response.GetBillWithMainFeedRes;
import com.everyones.lawmaking.domain.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, String> {
    @Query("SELECT new com.everyones.lawmaking.common.dto.BillDto(b.id, b.billName, c.name, bp.congressman.name, " +
            "b.summary, b.proposeDate, " +
            "(SELECT DISTINCT bp2.congressman.name FROM BillProposer bp2 WHERE bp2.bill = b)) " +
            "FROM Bill b " +
            "JOIN b.representProposer c " +
            "LEFT JOIN BillProposer bp ON bp.bill = b " +
            "WHERE (:lastProposeDate IS NULL AND :lastBillId IS NULL) OR " +
            "(:lastProposeDate IS NOT NULL AND b.proposeDate < :lastProposeDate) OR " +
            "(:lastProposeDate IS NOT NULL AND b.proposeDate = :lastProposeDate AND b.id < :lastBillId) " +
            "ORDER BY b.proposeDate DESC, b.id DESC")
    List<BillDto> findNext3Bills(@Param("lastBillId") String lastBillId, @Param("lastProposeDate") LocalDateTime lastProposeDate);

}
