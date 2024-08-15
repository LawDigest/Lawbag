package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.domain.entity.BillTimeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

}
