package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.common.dto.PromulgationDto;
import com.everyones.lawmaking.domain.entity.BillTimeline;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillTimelineRepository extends JpaRepository<BillTimeline,Long> {

    @Query("select bt.id from BillTimeline bt " +
            "where bt.actStatus=:actStatus and bt.bill.id =:billId and bt.billTimelineCommittee=:billTimelineCommittee and bt.billTimelineStage=:billTimelineStage and bt.statusUpdateDate=:statusUpdateDate")
    Optional<String> findBillTimelineByInfo(@Param("billId") String billId,
                                                  @Param("actStatus") String actStatus,
                                                  @Param("billTimelineStage") String billTimelineStage,
                                                  @Param("billTimelineCommittee") String billTimelineCommittee,
                                                  @Param("statusUpdateDate") LocalDate statusUpdateDate);

    // TODO: Grouping + Group_concat QueryDSL로 많이 사용하는 것 같아서 해당 부분 QueryDSL로 교체 요망
    @Query(value = "SELECT bill_timeline_committee AS committee, GROUP_CONCAT(bill_id) AS billIds " +
            "FROM BillTimeline " +
            "WHERE bill_timeline_stage = '위원회 심사' and status_update_date = :proposeDate " +
            "GROUP BY bill_timeline_committee " +
            "HAVING COUNT(DISTINCT bill_id) >= 1",
            nativeQuery = true)
    List<Tuple> findCommitteesWithMultipleBills(@Param("proposeDate") LocalDate proposeDate);

    @Query("select tl.bill.id from BillTimeline tl " +
            "where tl.statusUpdateDate = :proposeDate and tl.billTimelineStage = :stage")
    List<String> findBillTimelineBetweenProposeDateAndStage(@Param("proposeDate") LocalDate localDate, @Param("stage") String stage);

}
